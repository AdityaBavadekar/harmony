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

package com.adityabavadekar.harmony.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.MassUnits
import com.adityabavadekar.harmony.ui.common.activitybase.GoogleSigninActivity
import com.adityabavadekar.harmony.ui.main.MainActivity
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.auth.AuthManager
import com.adityabavadekar.harmony.utils.preferences.PreferencesKeys
import com.adityabavadekar.harmony.utils.preferences.preferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider

class SigninActivity : GoogleSigninActivity() {

    private val viewModel: SignInViewModel by viewModels<SignInViewModel> {
        SignInViewModel.Factory(application)
    }

    enum class SigninScreenType {
        GOOGLE_SIGNIN,
        USER_DOB,
        USER_BODY_MEASUREMENTS;

        fun next(): SigninScreenType? {
            return when (this) {
                GOOGLE_SIGNIN -> USER_DOB
                USER_DOB -> USER_BODY_MEASUREMENTS
                USER_BODY_MEASUREMENTS -> null
            }
        }

        fun previous(): SigninScreenType? {
            return when (this) {
                GOOGLE_SIGNIN -> null
                USER_DOB -> null
                USER_BODY_MEASUREMENTS -> USER_DOB
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HarmonyTheme {
                val uiState = viewModel.uiState.collectAsState().value

                Surface {
                    when (uiState.currentScreenType) {
                        SigninScreenType.GOOGLE_SIGNIN -> {
                            GoogleSignInScreen(
                                signinWithGoogle = {
                                    signInGoogle()
                                }
                            )
                        }

                        SigninScreenType.USER_DOB -> {
                            SignUpBirthdateInputScreen(
                                dateOfBirth = uiState.dateOfBirth,
                                updateDateOfBirth = { viewModel.updateDateOfBirth(it) },
                                onNext = { viewModel.onNext() },
                                onPrevious = {}
                            )
                        }

                        SigninScreenType.USER_BODY_MEASUREMENTS -> {
                            SignUpAfterScreen(
                                gender = uiState.gender,
                                onGenderChanged = { viewModel.updateGender(it) },
                                weight = uiState.weight,
                                weightUnit = uiState.weightUnit,
                                onWeightChanged = { value: Double, unit: MassUnits ->
                                    viewModel.onWeightChanged(
                                        value,
                                        unit
                                    )
                                },
                                height = uiState.height,
                                heightUnit = uiState.heightUnit,
                                onHeightChanged = { value: Double, unit: LengthUnits ->
                                    viewModel.onHeightChanged(
                                        value,
                                        unit
                                    )
                                },
                                onNext = {
                                    viewModel.saveAll()
                                    navigateToNextActivity()
                                },
                                onPrevious = { viewModel.onPrevious() }
                            )
                        }
                    }
                }

            }
        }
    }

    override fun onLoggedInWithGoogle(account: GoogleSignInAccount) {
        if (viewModel.uiState.value.isLoggedIn) return

        Log.d(
            TAG, "onLoggedInWithGoogle: Account(\n" +
                    "id=${account.id},\n" +
                    "idToken=${account.idToken},\n" +
                    "email=${account.email},\n" +
                    "displayName=${account.displayName},\n" +
                    "photoUrl=${account.photoUrl},\n" +
                    ")"
        )
        if (account.idToken != null) {
            Log.d(TAG, "onLoggedInWithGoogle: GOT an IdToken: ***")
            val cred = GoogleAuthProvider.getCredential(account.idToken, null)
            AuthManager.signinWithCredential(cred) {
                Log.i(TAG, "onLoggedInWithGoogle: Firebase.googleAuth ??? $it")
                if (it) {
                    preferencesManager.setString(PreferencesKeys.USER_EMAIL, account.email)
                    viewModel.addAccount(AuthManager.requireUser().uid, account)
                } else {
                    Log.e(
                        TAG,
                        "onLoggedInWithGoogle: AuthManager.signinWithCredential --> returned False"
                    )
                    signOutGoogle()
                }
            }
        } else {
            Log.e(TAG, "onLoggedInWithGoogle: No!!! IdToken!!")
            signOutGoogle()
            return
        }

    }

    override fun onGoogleSigninFailure(errorCode: Int) {
        Log.e(
            TAG, "onGoogleSigninFailure: errorCode=$errorCode"
        )
    }

    override fun onGoogleSigninLogoutCompleted() {
        Log.d(TAG, "onGoogleSigninLogoutCompleted: Success")
    }

    override fun onGoogleSigninLogoutFailed(e: Exception) {
        Log.e(TAG, "onGoogleSigninLogoutFailed: Error", e)
    }

    private fun navigateToNextActivity() {
        startActivity(Intent(this@SigninActivity, MainActivity::class.java))
        finishAffinity()
    }

    override fun onStart() {
        super.onStart()
        getLastSigninAccount()?.let { onLoggedInWithGoogle(it) }
    }

    companion object {
        private const val TAG = "[SigninActivity]"
    }

}