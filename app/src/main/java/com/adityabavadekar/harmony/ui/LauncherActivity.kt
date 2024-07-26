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

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.adityabavadekar.harmony.ui.common.activitybase.GoogleSigninActivity.Companion.getLastSigninAccount
import com.adityabavadekar.harmony.ui.main.MainActivity
import com.adityabavadekar.harmony.ui.onboarding.WelcomeScreen
import com.adityabavadekar.harmony.ui.signin.SigninActivity
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.preferences.PreferencesKeys
import com.adityabavadekar.harmony.utils.preferences.preferencesManager

class LauncherActivity : ComponentActivity() {

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
        }


    }
}
