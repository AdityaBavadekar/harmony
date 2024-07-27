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

package com.adityabavadekar.harmony.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class PermissionUtils {

    class PermissionsNotGrantedException(permissions: List<String>) :
        Exception("Permissions `${permissions.joinToString()}}` not granted")

    companion object {

        fun locationPermissions(): List<String> {
            return listOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun notificationPermissions(): List<String> {
            return listOf(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        fun areNotificationsAllowed(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return areAllGranted(notificationPermissions(), context)
            }
            return true
        }

        fun isGranted(permission: String, context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun areAllGranted(permissions: List<String>, context: Context): Boolean {
            var allGranted = false
            permissions.forEach {
                allGranted = ActivityCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
            return allGranted
        }

    }
}