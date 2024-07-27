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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityabavadekar.harmony.data.model.UserRecord
import com.adityabavadekar.harmony.database.repo.AccountRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    fun addAccount(repo: AccountRepository, account: GoogleSignInAccount) {
        viewModelScope.launch {
            repo.addAccount(UserRecord.fromGoogleAccount(account))
        }
    }


    private var _uiState: MutableStateFlow<SignInUiState?> = MutableStateFlow(null)
    val uiState: StateFlow<SignInUiState?> = _uiState.asStateFlow()

}