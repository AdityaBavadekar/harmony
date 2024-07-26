/*
 * Copyright 2024 Aditya Bavadekar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adityabavadekar.harmony.ui.livetracking.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.adityabavadekar.harmony.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@SuppressLint("LogConditional")
class LiveLocationClient(
    private val context: Context,
    private val updateListener: LocationUpdateListener,
) : LocationClient {
    private val client: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context.applicationContext)

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult.locations.size > 1) {
                Log.d(TAG, "Location: [UPDATE] SIZE MORE THAN 1 = ${locationResult.locations.size}")
            }
            locationResult.locations.firstOrNull()?.let {
                updateListener.onLocationUpdate(it)
            }
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            super.onLocationAvailability(locationAvailability)
            Log.d(
                TAG,
                "onLocationAvailability: isLocationAvailable=${locationAvailability.isLocationAvailable}"
            )
        }

    }

    @SuppressLint("MissingPermission")
    override fun startLocationRetrieval(locationInterval: Long) {
        Log.d(TAG, "startLocationRetrieval")

        if (!PermissionUtils.areAllGranted(PermissionUtils.locationPermissions(), context)) {
            throw PermissionUtils.PermissionsNotGrantedException(PermissionUtils.locationPermissions())
        }

        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        Log.d(TAG, "startLocationRetrieval: GPS=${gpsEnabled} NETWORK=${networkEnabled}")

        if (!gpsEnabled && !networkEnabled) {
            throw LocationClient.LocationException("GPS is not enabled")
        }

        val locationRequest = LocationRequest.Builder(locationInterval)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        client.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    override fun destroy() {
        Log.d(TAG, "destroy")
        client.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val TAG = "[LiveLocationClient]"
    }
}

interface LocationClient {
    fun startLocationRetrieval(locationInterval: Long)

    fun destroy()

    class LocationException(message: String) : Exception(message)
}


interface LocationUpdateListener {
    fun onLocationUpdate(location: Location)
}
