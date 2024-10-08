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
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.livetracking.GeoLocation
import com.adityabavadekar.harmony.utils.PermissionUtils

class LiveTrackerService : Service(), LocationUpdateListener {

    private lateinit var locationClient: LocationClient

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        locationClient = LiveLocationClient(applicationContext, this)
        start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action}")
        when (intent?.action) {
            LOC_ACTION_START -> start()
            LOC_ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun start() {
        Log.d(TAG, "start: Starting")
        startSelf()
    }

    private fun stop() {
        Log.d(TAG, "stop: Stopping")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun startSelf() {
        val notification = buildNotification(applicationContext)
        locationClient.startLocationRetrieval(LOCATION_UPDATES_INTERVAL)

        if (PermissionUtils.areNotificationsAllowed(this)) {
            startForeground(LOCATION_NOTIFICATION_ID, notification.build())
        }
    }

    @SuppressLint("MissingPermission", "NewApi")
    override fun onLocationUpdate(location: Location) {
        Log.d(TAG, "onLocationUpdate: ${GeoLocation.from(location)}")
        val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)

        val updatedNotification = buildNotification(applicationContext)
            .setContentText("Current location: ${location.latitude}, ${location.longitude}")

        if (PermissionUtils.areNotificationsAllowed(this)) {
            notificationManagerCompat.notify(
                LOCATION_NOTIFICATION_ID,
                updatedNotification.build()
            )
        } else {
            Log.w(
                TAG,
                "onLocationUpdate: Notification Permission not granted!",
                PermissionUtils.PermissionsNotGrantedException(PermissionUtils.notificationPermissions())
            )
        }
        forwardLocationUpdates(location)
    }

    private fun forwardLocationUpdates(location: Location) {
        val locationUpdatesIntent = Intent(LOC_UPDATE_INTENT_ACTION).apply {
            val dataBundle = Bundle()
            dataBundle.putParcelable(LOC_UPDATE_INTENT_BUNDLE_LOC_KEY, location)
            putExtra(LOC_UPDATE_INTENT_BUNDLE, dataBundle)
        }
        sendBroadcast(locationUpdatesIntent)

    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient.destroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "[LiveTrackerService]"
        private const val LOCATION_NOTIFICATION_ID = 101
        private const val LOCATION_UPDATES_INTERVAL = 300L //ms
        const val LOC_UPDATE_INTENT_ACTION =
            "com.adityabavadekar.harmony.intent.action.location_update"
        const val LOC_UPDATE_INTENT_BUNDLE_LOC_KEY = "location"
        const val LOC_UPDATE_INTENT_BUNDLE = "location_update_data"
        const val LOCATION_NOTIFICATION_CHANNEL_ID = "location_channel"
        const val LOC_ACTION_START = "action_start"
        const val LOC_ACTION_STOP = "action_stop"

        internal fun buildNotification(context: Context): NotificationCompat.Builder {
            val notification = NotificationCompat.Builder(context, LOCATION_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Location tracking is live")
                .setContentText("Tracking location in background, feel free to close the app.")
                .setSmallIcon(R.drawable.location_pin)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.Cyan.toArgb())
                .setOngoing(true)
                .setAutoCancel(false)
            return notification
        }

        fun decodeLocationUpdatesIntent(intent: Intent?): Location? {
            if (intent == null || !intent.hasExtra(LOC_UPDATE_INTENT_BUNDLE)) return null

            val bundle = intent.getBundleExtra(LOC_UPDATE_INTENT_BUNDLE) ?: return null
            return bundle.getParcelable(LOC_UPDATE_INTENT_BUNDLE_LOC_KEY)
        }
    }
}