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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.adityabavadekar.harmony.ui.common.activitybase.GoogleSigninActivity
import com.adityabavadekar.harmony.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class SigninActivity : GoogleSigninActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var signUpScreenType by remember { mutableStateOf(SignUpScreenType.NONE) }
            var isSignInScreen by remember { mutableStateOf(true) }

            when (isSignInScreen) {
                true -> {
                    SignInScreen(
                        signinWithGoogle = {
                            signInGoogle()
                        },
                        switchToSignUp = {
                            isSignInScreen = false
                        }
                    )
                }

                false -> {
                    if (hasNextSignUpScreen(signUpScreenType)) {
                        GetNextSignUpScreen(
                            signUpScreenType = signUpScreenType,
                            onNext = {
                                signUpScreenType = it
                                Log.d(TAG, "onCreate SCREEN::[onNext](current=${it.name})")
                            },
                            onPrevious = {
                                Log.d(TAG, "onCreate SCREEN::[onPrevious](current=${it.name})")
                                if (it == SignUpScreenType.EMAIL) {
                                    isSignInScreen = true
                                    signUpScreenType = SignUpScreenType.NONE
                                } else {
                                    signUpScreenType = it
                                }
                            }
                        )
                    } else {
                        //Completed Profile
                        Log.d(TAG, "onCreate: USER SIGN UP PROFILE COMPLETED!")
                        onLoggedIn()
                    }
                }
            }
        }
    }

    override fun onLoggedInWithGoogle(account: GoogleSignInAccount) {
        Log.d(
            TAG, "onLoggedInWithGoogle: Account(\n" +
                    "id=${account.id},\n" +
                    "email=${account.email},\n" +
                    "displayName=${account.displayName},\n" +
                    "photoUrl=${account.photoUrl},\n" +
                    ")"
        )
        onLoggedIn()
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

    private fun onLoggedIn() {
        startActivity(Intent(this@SigninActivity, MainActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        getLastSigninAccount()?.let {
            onLoggedInWithGoogle(it)
            onLoggedIn()
        }
    }

}