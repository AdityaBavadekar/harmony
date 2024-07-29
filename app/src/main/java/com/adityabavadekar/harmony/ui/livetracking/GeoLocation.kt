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

package com.adityabavadekar.harmony.ui.livetracking

import android.location.Location
import com.adityabavadekar.harmony.ui.common.Speed
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import kotlin.math.pow

class GeoLocation(
    private val lat: Double,
    private val lon: Double,
    val altitudeMeters: Double? = null,
    private var speedMs: Double? = null,
    val accuracy: Float = 1f,
    private val horizontalDisplacement: Float? = null,
    val timestamp: Long,
) : IGeoPoint {
    @Deprecated("Deprecated in Java")
    override fun getLatitudeE6(): Int {
        return lat.toInt()
    }

    @Deprecated("Deprecated in Java")
    override fun getLongitudeE6(): Int {
        return lon.toInt()
    }

    override fun getLatitude(): Double = lat

    override fun getLongitude(): Double = lon

    override fun toString(): String {
        val s = StringBuilder("Location[$lat, $lon]")
        s.append(" Accuracy[${accuracy ?: 0}]")
        s.append(" Speed[${speedMs ?: 0}m/s]")
        s.append(" hDisplacement[${horizontalDisplacement ?: 0}m]")
        s.append(" Altitude[${altitudeMeters ?: 0}m]")
        return s.toString()
    }

    val geoPoint: GeoPoint
        get() = GeoPoint(lat, lon, altitudeMeters ?: 0.00)

    fun hasHorizontalDisplacement(): Boolean {
        return horizontalDisplacement != null
    }

    fun hasSpeed(): Boolean {
        return speedMs != null
    }

    fun speedOrNull(): Double? {
        return speedMs
    }

    val requireSpeed: Speed
        get() = Speed(speedMs!!)

    val requireHorizontalDisplacement: Float
        get() = horizontalDisplacement!!

    companion object {
        fun from(location: Location): GeoLocation {
            return GeoLocation(
                lat = location.latitude,
                lon = location.longitude,
                altitudeMeters = if (location.hasAltitude()) location.altitude else 0.0,
                speedMs = if (location.hasSpeed()) location.speed.toDouble() else 0.0,
                accuracy = if (location.hasAccuracy()) location.accuracy else 0.0f,
                horizontalDisplacement = if (location.hasBearing()) location.bearing else null,
                timestamp = (location.elapsedRealtimeNanos / 10.0.pow(6.0)).toLong()
            )
        }

        fun empty(): GeoLocation {
            return GeoLocation(
                lat = 0.0,
                lon = 0.0,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}