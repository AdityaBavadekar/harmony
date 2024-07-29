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

package com.adityabavadekar.harmony.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.LauncherActivity
import com.adityabavadekar.harmony.ui.common.activitybase.GoogleSigninActivity
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.showErrorDialog
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class SettingsActivity : GoogleSigninActivity() {

    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            HarmonyTheme {
                SettingsScreen(
                    uiState = uiState,
                    onLogoutClicked = {
                        signOutGoogle()
                    },
                    onShouldNavigateBack = {
                        finish()
                    }
                )
            }
        }
    }

    override fun onLoggedInWithGoogle(account: GoogleSignInAccount) {}
    override fun onGoogleSigninFailure(errorCode: Int) {}

    override fun onGoogleSigninLogoutCompleted() {
        startActivity(Intent(this, LauncherActivity::class.java))
        finishAffinity() //Finish all activities bellow this activity including this activity
    }

    override fun onGoogleSigninLogoutFailed(e: Exception) {
        Log.e(TAG, "onGoogleSigninLogoutFailed", e)
        showErrorDialog(
            title = getString(R.string.something_went_wrong),
            message = getString(R.string.google_signout_failed_message)
        )
    }

    companion object {
        private const val TAG = "[SettingsActivity]"
    }
}