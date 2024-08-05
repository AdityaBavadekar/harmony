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

package com.adityabavadekar.harmony.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.adityabavadekar.harmony.ui.common.activitybase.GoogleSigninActivity.Companion.getLastSigninAccount
import com.adityabavadekar.harmony.ui.common.activitybase.PermissionActivityV2
import com.adityabavadekar.harmony.ui.main.MainActivity
import com.adityabavadekar.harmony.ui.onboarding.WelcomeScreen
import com.adityabavadekar.harmony.ui.permissions.PermissionsRationaleActivity
import com.adityabavadekar.harmony.ui.signin.SigninActivity
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.preferences.PreferencesKeys
import com.adityabavadekar.harmony.utils.preferences.preferencesManager

class LauncherActivity : PermissionActivityV2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isOnboardingCompleted =
            preferencesManager.getBoolean(PreferencesKeys.ONBOARDING_COMPLETED, false)

        val nextIntentClass = if (getLastSigninAccount() == null) SigninActivity::class.java
        else MainActivity::class.java
        if (isOnboardingCompleted) {
            startActivity(Intent(this, nextIntentClass))
            finish()
        } else {
            setContent {
                HarmonyTheme(darkTheme = false, dynamicColor = false) {
                    WelcomeScreen(
                        onCompleted = {
                            preferencesManager.setBoolean(
                                PreferencesKeys.ONBOARDING_COMPLETED,
                                true
                            )
                            startActivity(Intent(this, nextIntentClass))
                            finish()
                        }
                    )
                }
            }
            requestAllPermissions()
        }
    }

    private fun requestAllPermissions() {
        val permissionsToAsk = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionsToAsk.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToAsk.add(Manifest.permission.POST_NOTIFICATIONS)
        }
//        permissionsToAsk.addAll(PermissionUtils.locationPermissions())
        requestMultiple(permissionsToAsk) {
            /*listener(Manifest.permission.ACCESS_FINE_LOCATION) {
                doOnGranted {
                    Log.d(TAG, "onGranted: ACCESS_FINE_LOCATION")
                }
                doOnDenied {
                    Log.d(TAG, "onDenied: ACCESS_FINE_LOCATION")
                    false
                }
            }
            listener(Manifest.permission.ACCESS_COARSE_LOCATION) {
                doOnGranted {
                    Log.d(TAG, "onGranted: ACCESS_COARSE_LOCATION")
                }
                doOnDenied {
                    Log.d(TAG, "onDenied: ACCESS_COARSE_LOCATION")
                    false
                }
            }*/
            listener(Manifest.permission.ACTIVITY_RECOGNITION) {
                doOnGranted {
                    Log.d(TAG, "onGranted: ACTIVITY_RECOGNITION")
                }
                doOnDenied {
                    Log.d(TAG, "onDenied: ACTIVITY_RECOGNITION")
                    false
                }
                doOnShouldShowPermissionUI {
                    Log.d(TAG, "OnShouldShowPermissionUI: ACTIVITY_RECOGNITION")
                    PermissionsRationaleActivity.startActivityRecognitionPermissionRationale(
                        applicationContext
                    )
                    false
                }
            }
            listener(Manifest.permission.POST_NOTIFICATIONS) {
                doOnGranted {
                    Log.d(TAG, "onGranted: POST_NOTIFICATIONS")
                }
                doOnDenied {
                    Log.d(TAG, "onDenied: POST_NOTIFICATIONS")
                    false
                }
                doOnShouldShowPermissionUI {
                    Log.d(TAG, "OnShouldShowPermissionUI: POST_NOTIFICATIONS")
                    PermissionsRationaleActivity.startNotificationPermissionRationale(
                        applicationContext
                    )
                    false
                }
            }
        }
    }

    companion object {
        private const val TAG = "[LauncherActivity]"
    }
}
