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

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class PermissionActivity : ComponentActivity() {

    private var _permission: String? = null

    abstract fun onGranted(permission: String)
    abstract fun onDenied(permission: String)
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

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission
            ) -> onShouldShowPermissionUI(permission)

            else -> {
                _permission = permission
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    companion object{
        private const val TAG = "[PermissionActivity]"
    }
}