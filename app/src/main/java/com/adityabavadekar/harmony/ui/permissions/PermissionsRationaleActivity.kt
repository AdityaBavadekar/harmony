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

package com.adityabavadekar.harmony.ui.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.adityabavadekar.harmony.ui.common.activitybase.PermissionActivityV2
import com.adityabavadekar.harmony.utils.PermissionUtils

class PermissionsRationaleActivity : PermissionActivityV2() {

    private var areSettingsOpened: Boolean = false
    private lateinit var permissionType: String


    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        setContent {
            val openSettingsDialog = intent.hasExtra(EXTRA_OPT_IN_OPEN_SETTINGS)
            permissionType = requireNotNull(intent.getStringExtra(EXTRA_PERMISSION_TYPE))

            when (permissionType) {
                EXTRA_PERMISSION_TYPE_LOCATION -> {
                    if (openSettingsDialog) {
                        OpenSettingsLocationPermissionDialogContent(
                            onPositiveButtonClick = {
                                areSettingsOpened = true
                                openAppSettings()
                            },
                            onNegativeButtonClick = {
                                onDenied()
                            }
                        )
                    } else {
                        LocationPermissionDialogContent(
                            onPositiveButtonClick = {
                                requestMultiple(PermissionUtils.locationPermissions()) {
                                    listener(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        permissionListener()
                                    )
                                    listener(
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        permissionListener()
                                    )
                                }
                            },
                            onNegativeButtonClick = {
                                onDenied()
                            }
                        )
                    }
                }

                EXTRA_PERMISSION_TYPE_ACTIVITY -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        onGranted()
                    }
                    if (openSettingsDialog) {
                        OpenSettingsActivityRecognitionPermissionDialogContent(
                            onPositiveButtonClick = {
                                areSettingsOpened = true
                                openAppSettings()
                            },
                            onNegativeButtonClick = {
                                onDenied()
                            }
                        )
                    } else {
                        ActivityRecognitionPermissionDialogContent(
                            onPositiveButtonClick = {
                                requestPermission(
                                    Manifest.permission.ACTIVITY_RECOGNITION,
                                    permissionListener()
                                )
                            },
                            onNegativeButtonClick = {
                                onDenied()
                            }
                        )
                    }
                }

                EXTRA_PERMISSION_TYPE_NOTIFICATIONS -> {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        onGranted() // Notification permissions not required
                    }
                    if (openSettingsDialog) {
                        OpenSettingsNotificationPermissionDialogContent(
                            onPositiveButtonClick = {
                                areSettingsOpened = true
                                openAppSettings()
                            },
                            onNegativeButtonClick = {
                                onDenied()
                            }
                        )
                    } else {
                        NotificationPermissionDialogContent(
                            onPositiveButtonClick = {
                                requestPermission(
                                    Manifest.permission.POST_NOTIFICATIONS,
                                    permissionListener()
                                )
                            },
                            onNegativeButtonClick = {
                                onDenied()
                            }
                        )
                    }
                }

                else -> throw IllegalStateException()
            }
        }
    }

    private fun onGranted(data: Intent = Intent()) {
        setResult(PackageManager.PERMISSION_GRANTED, data)
        finish()
    }

    private fun onDenied(data: Intent = Intent()) {
        setResult(PackageManager.PERMISSION_DENIED, data)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (areSettingsOpened) {
            val data = Intent().apply { putExtra(EXTRA_FROM_SETTINGS, true) }
            when (permissionType) {
                EXTRA_PERMISSION_TYPE_LOCATION -> {
                    if (PermissionUtils.isLocationPermissionGranted(applicationContext)) {
                        onGranted(data)
                    } else onDenied(data)
                }

                EXTRA_PERMISSION_TYPE_NOTIFICATIONS -> {
                    if (PermissionUtils.isGranted(
                            Manifest.permission.POST_NOTIFICATIONS,
                            applicationContext
                        )
                    ) {
                        onGranted(data)
                    } else onDenied(data)
                }

                EXTRA_PERMISSION_TYPE_ACTIVITY -> {
                    if (PermissionUtils.isGranted(
                            Manifest.permission.ACTIVITY_RECOGNITION,
                            applicationContext
                        )
                    ) {
                        onGranted(data)
                    } else onDenied(data)
                }
            }
        }
    }

    private fun permissionListener(): PermissionResultListener.Builder.() -> PermissionResultListener.Builder {
        return {
            doOnGranted { onGranted() }
            doOnDenied {
                onDenied()
                false
            }
            doOnShouldShowPermissionUI {
                Log.d(TAG, "permissionListener: doOnShouldShowPermissionUI")
                true
            }
        }
    }

    companion object {
        private const val TAG = "[PermissionsRationaleActivity]"
        private const val EXTRA_PERMISSION_TYPE = "permission.type"
        private const val EXTRA_OPT_IN_OPEN_SETTINGS = "permission.open_settings"
        private const val EXTRA_PERMISSION_TYPE_LOCATION = "permission_type.location"
        private const val EXTRA_PERMISSION_TYPE_ACTIVITY = "permission_type.activity"
        private const val EXTRA_PERMISSION_TYPE_NOTIFICATIONS = "permission_type.notifications"
        const val EXTRA_FROM_SETTINGS = "from_settings"
        const val REQUEST_CODE_LOCATION_RATIONALE = 55
        const val REQUEST_CODE_NOTIFICATION_RATIONALE = 65
        const val REQUEST_CODE_ACTIVITY_RATIONALE = 75

        private fun startPermissionActivityIntent(
            context: Context,
            permissionType: String,
            requestCode: Int,
            openSettingsDialog: Boolean
        ) {
            (context as Activity).startActivityForResult(
                Intent(context, PermissionsRationaleActivity::class.java).apply {
                    putExtra(EXTRA_PERMISSION_TYPE, permissionType)
                    if (openSettingsDialog) putExtra(EXTRA_OPT_IN_OPEN_SETTINGS, true)
                    //addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                requestCode
            )
        }

        fun startLocationPermissionRationale(
            context: Context,
            openSettingsDialog: Boolean = false
        ) = startPermissionActivityIntent(
            context,
            EXTRA_PERMISSION_TYPE_LOCATION,
            REQUEST_CODE_LOCATION_RATIONALE,
            openSettingsDialog
        )

        fun startActivityRecognitionPermissionRationale(
            context: Context,
            openSettingsDialog: Boolean = false
        ) = startPermissionActivityIntent(
            context,
            EXTRA_PERMISSION_TYPE_ACTIVITY,
            REQUEST_CODE_ACTIVITY_RATIONALE,
            openSettingsDialog
        )

        fun startNotificationPermissionRationale(
            context: Context,
            openSettingsDialog: Boolean = false
        ) = startPermissionActivityIntent(
            context,
            EXTRA_PERMISSION_TYPE_NOTIFICATIONS,
            REQUEST_CODE_NOTIFICATION_RATIONALE,
            openSettingsDialog
        )
    }
}