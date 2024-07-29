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

@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package com.adityabavadekar.harmony.ui.common.activitybase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.adityabavadekar.harmony.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


/**
 * Functions:
 *   - signInGoogle()
 *   - signOutGoogle()
 *   - getLastSigninAccount()
 *
 * */
abstract class GoogleSigninActivity : ComponentActivity() {
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private val signinRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let { data ->
                onRequestResult(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseOnCreate()
    }

    private fun initialiseOnCreate() {
        mGoogleSignInClient = buildClient(this)
    }

    fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient!!.signInIntent
        signinRequestLauncher.launch(signInIntent)
        //startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            data?.let { onRequestResult(it) }
        }
    }

    private fun onRequestResult(data: Intent) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        handleSignInResult(task)
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            Log.i(TAG, "signInResult:Signed in successfully")
            updateUI(account, errorCode = null)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(account = null, errorCode = e.statusCode)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?, errorCode: Int?) {
        if (account == null) {
            onGoogleSigninFailure(requireNotNull(errorCode))
        } else {
            onLoggedInWithGoogle(account)
        }
    }

    abstract fun onLoggedInWithGoogle(account: GoogleSignInAccount)
    abstract fun onGoogleSigninFailure(errorCode: Int)
    abstract fun onGoogleSigninLogoutCompleted()
    abstract fun onGoogleSigninLogoutFailed(e: Exception)

    fun signOutGoogle() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                onGoogleSigninLogoutCompleted()
            }
            .addOnFailureListener {
                onGoogleSigninLogoutFailed(it)
            }
    }

    companion object {
        private const val TAG = "[GoogleSigninActivity]"
        private const val GOOGLE_SIGN_IN_CODE = 21

        /*
        @Suppress("LocalVariableName")
        private val user_gender_scope = Scope("https://www.googleapis.com/auth/user.gender.read")
        private val user_dob_scope = Scope("https://www.googleapis.com/auth/user.birthday.read")
        */
        fun Context.getLastSigninAccount(): GoogleSignInAccount? {
            return GoogleSignIn.getLastSignedInAccount(this)
        }

        fun buildClient(activity: Activity): GoogleSignInClient {
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.applicationContext.getString(R.string.default_web_client_id))
                //.requestScopes(user_gender_scope, user_dob_scope)
                .requestProfile()
                .requestEmail()
                .build()

            // [START build_client]
            // Build a GoogleSignInClient with the options specified by gso.
            return GoogleSignIn.getClient(activity, gso);
            // [END build_client]
        }

    }

}