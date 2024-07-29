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

package com.adityabavadekar.harmony.utils.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.gson.Gson

object AuthManager {

    private val auth = Firebase.auth

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun requireUser(): FirebaseUser {
        return requireNotNull(auth.currentUser) { ".requireUser() called while user was not logged in" }
    }

    fun signinWithCredential(cred: AuthCredential, onCompleted: (loggedIn: Boolean) -> Unit) {
        auth.signInWithCredential(cred)
            .addOnSuccessListener {
                Log.d(TAG, "signinWithCredential: SUCCESS: **** \n(data=${Gson().toJson(it)})")
                onCompleted(true)
            }
            .addOnFailureListener {
                Log.e(TAG, "signinWithCredential: Failed!!", it)
                onCompleted(false)
            }
    }

    private const val TAG = "[AuthManager]"
}