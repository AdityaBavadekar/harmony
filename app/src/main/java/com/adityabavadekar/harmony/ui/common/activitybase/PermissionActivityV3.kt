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

package com.adityabavadekar.harmony.ui.common.activitybase

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adityabavadekar.harmony.ui.permissions.PermissionsRationaleActivity
import com.adityabavadekar.harmony.utils.preferences.PreferencesKeys
import com.adityabavadekar.harmony.utils.preferences.preferencesManager

/**
 * An abstract activity that handles permission requests.
 * Subclasses must implement the abstract methods to handle
 * different permission states.
 */
abstract class PermissionActivityV3 : ComponentActivity() {

    open fun onLocationPermissionResult(isGranted: Boolean, fromSettings: Boolean = false) {}
    open fun onNotificationPermissionResult(isGranted: Boolean, fromSettings: Boolean = false) {}
    open fun onActivityPermissionResult(isGranted: Boolean, fromSettings: Boolean = false) {}

    private fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
    }

    private fun hasAsked(key: PreferencesKeys) = preferencesManager.getBoolean(key, false)
    private fun setAsked(key: PreferencesKeys) = preferencesManager.setBoolean(key, true)

    fun askLocationPermissions() {
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION

        if (isGranted(fineLocationPermission) || isGranted(coarseLocationPermission)) {
            Log.d(TAG, "askLocationPermissions: GRANTED")
            onLocationPermissionResult(true)
            return
        }

        if (shouldShowRequestRationale(fineLocationPermission) || shouldShowRequestRationale(
                coarseLocationPermission
            )
        ) {
            // permission is denied only once
            //Show dialog
            Log.d(TAG, "askLocationPermissions: SHOW RATIONALE")
            PermissionsRationaleActivity.startLocationPermissionRationale(this)
            return
        }

        if (!hasAsked(PreferencesKeys.ASKED_LOCATION_PERMISSION)) {
            Log.d(TAG, "askLocationPermissions: NOT ASKED --> ASKING NOW")
            setAsked(PreferencesKeys.ASKED_LOCATION_PERMISSION)
            launchPermission(fineLocationPermission, REQUEST_CODE_LOCATION)
            return
        }

        // else permission is denied 2 times
        // show permission is required for app. Provide link to open settings.
        Log.d(TAG, "askLocationPermissions: DENIED TWICE --> SHOWING OPEN SETTINGS RATIONALE")
        PermissionsRationaleActivity.startLocationPermissionRationale(
            this,
            openSettingsDialog = true
        )
    }

    fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            onNotificationPermissionResult(true)
            return
        }

        val postNotificationsPermission = android.Manifest.permission.POST_NOTIFICATIONS
        if (isGranted(postNotificationsPermission)) {
            onNotificationPermissionResult(true)
            return
        }

        if (shouldShowRequestRationale(postNotificationsPermission)) {
            // permission is denied only once
            //Show dialog
            PermissionsRationaleActivity.startNotificationPermissionRationale(this)
            return
        }

        if (!hasAsked(PreferencesKeys.ASKED_NOTIFICATION_PERMISSION)) {
            setAsked(PreferencesKeys.ASKED_NOTIFICATION_PERMISSION)
            launchPermission(postNotificationsPermission, REQUEST_CODE_NOTIFICATION)
            return
        }

        // else permission is denied 2 times
        // show permission is required for app. Provide link to open settings.
        PermissionsRationaleActivity.startNotificationPermissionRationale(
            this,
            openSettingsDialog = true
        )
    }

    fun askActivityPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            onActivityPermissionResult(true)
            return
        }

        val activityPermission = android.Manifest.permission.ACTIVITY_RECOGNITION
        if (isGranted(activityPermission)) {
            onActivityPermissionResult(true)
            return
        }

        if (shouldShowRequestRationale(activityPermission)) {
            // permission is denied only once
            //Show dialog
            PermissionsRationaleActivity.startActivityRecognitionPermissionRationale(
                this
            )
            return
        }

        if (!hasAsked(PreferencesKeys.ASKED_ACTIVITY_PERMISSION)) {
            setAsked(PreferencesKeys.ASKED_ACTIVITY_PERMISSION)
            launchPermission(activityPermission, REQUEST_CODE_ACTIVITY)
            return
        }

        // else permission is denied 2 times
        // show permission is required for app. Provide link to open settings.
        PermissionsRationaleActivity.startActivityRecognitionPermissionRationale(
            this,
            openSettingsDialog = true
        )
    }

    private fun launchPermission(permission: String, requestCode: Int) {
        requestPermissions(
            arrayOf(permission),
            requestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(
            TAG,
            "onRequestPermissionsResult: " +
                    "requestCode=$requestCode " +
                    "permissions=${permissions.joinToString()} " +
                    "grantResults=${grantResults.joinToString()}"
        )
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun isPermissionGranted() = grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED

        when (requestCode) {
            REQUEST_CODE_LOCATION -> onLocationPermissionResult(isPermissionGranted())
            REQUEST_CODE_NOTIFICATION -> onNotificationPermissionResult(isPermissionGranted())
            REQUEST_CODE_ACTIVITY -> onActivityPermissionResult(isPermissionGranted())
            else -> {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fun isPermissionGranted() = resultCode == PackageManager.PERMISSION_GRANTED

        val fromSettings =
            data?.getBooleanExtra(PermissionsRationaleActivity.EXTRA_FROM_SETTINGS, false) ?: false
        when (requestCode) {
            PermissionsRationaleActivity.REQUEST_CODE_LOCATION_RATIONALE -> onLocationPermissionResult(
                isPermissionGranted(),
                fromSettings
            )

            PermissionsRationaleActivity.REQUEST_CODE_NOTIFICATION_RATIONALE -> onNotificationPermissionResult(
                isPermissionGranted(),
                fromSettings
            )

            PermissionsRationaleActivity.REQUEST_CODE_ACTIVITY_RATIONALE -> onActivityPermissionResult(
                isPermissionGranted(),
                fromSettings
            )

            else -> {}
        }
    }

    companion object {
        private const val TAG = "[PermissionActivityV2]"
        private const val REQUEST_CODE_LOCATION = 101
        private const val REQUEST_CODE_NOTIFICATION = 201
        private const val REQUEST_CODE_ACTIVITY = 301
    }
}