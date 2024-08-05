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
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * An abstract activity that handles permission requests.
 * Subclasses must implement the abstract methods to handle
 * different permission states.
 */
abstract class PermissionActivity : ComponentActivity() {

    private var _permission: String? = null

    /**
     * Called when the requested permission has been granted.
     *
     * @param permission The permission that was granted.
     */
    abstract fun onGranted(permission: String)

    /**
     * Called when the requested permission has been denied.
     *
     * @param permission The permission that was denied.
     */
    abstract fun onDenied(permission: String)

    /**
     * Called when the UI should show an explanation before requesting the permission.
     *
     * @param permission The permission for which the UI should be shown.
     */
    abstract fun onShouldShowPermissionUI(permission: String)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onGranted(_permission!!)
            } else {
                onDenied(_permission!!)
            }
        }

    private val multipleRequestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
            permissionMap.forEach { (permissionString, granted) ->
                if (granted) onGranted(permissionString)
                else onDenied(permissionString)
            }
        }

    fun requestMultiple(permissions: List<String>) {
        Log.i(TAG, "requestMultiple: Asking [$permissions]")
        multipleRequestPermissionLauncher.launch(permissions.toTypedArray())
    }

    fun requestPermission(permission: String) {
        Log.i(TAG, "requestPermission: Asking '$permission'")

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> onGranted(permission)

            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                onShouldShowPermissionUI(permission)
                requestPermissionLauncher.launch(permission)
            }

            else -> {
                _permission = permission
            }
        }
    }

    private fun openAppSettings() {

        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addCategory(CATEGORY_DEFAULT)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            addFlags(FLAG_ACTIVITY_NO_HISTORY)
            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        startActivity(intent)
    }

    companion object {
        private const val TAG = "[PermissionActivity]"
    }
}