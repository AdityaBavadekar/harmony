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

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.adityabavadekar.harmony.data.model.UserFitnessRecord
import com.adityabavadekar.harmony.data.model.UserRecord
import com.adityabavadekar.harmony.database.repo.AccountRepository
import com.adityabavadekar.harmony.ui.common.Gender
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.Mass
import com.adityabavadekar.harmony.ui.common.MassUnits
import com.adityabavadekar.harmony.utils.asHarmonyApp
import com.adityabavadekar.harmony.utils.withIOContext
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private var _uiState: MutableStateFlow<SignInUiState> = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun addAccount(uid: String, account: GoogleSignInAccount) = viewModelScope.launch {
        val userRecord = UserRecord.fromGoogleAccount(uid, account)
        withIOContext {
            accountRepository.addAccount(userRecord)
        }
        _uiState.value = _uiState.value.copy(
            email = userRecord.email,
            name = userRecord.getName(),
            currentScreenType = _uiState.value.currentScreenType.next()!!,
            isLoggedIn = true
        )
    }

    fun onNext() {
        val screenType = _uiState.value.currentScreenType
        val nextScreenType = screenType.next()
        if (nextScreenType != null) _uiState.value =
            _uiState.value.copy(currentScreenType = nextScreenType)
    }

    fun onPrevious() {
        val screenType = _uiState.value.currentScreenType
        val prevScreenType = screenType.previous()
        if (prevScreenType != null) _uiState.value =
            _uiState.value.copy(currentScreenType = prevScreenType)
    }

    fun updateDateOfBirth(value: Long) {
        _uiState.value = _uiState.value.copy(dateOfBirth = value)
    }

    fun onWeightChanged(value: Double, unit: MassUnits) {
        _uiState.value = _uiState.value.copy(weight = value, weightUnit = unit)
    }

    fun onHeightChanged(value: Double, unit: LengthUnits) {
        _uiState.value = _uiState.value.copy(height = value, heightUnit = unit)
    }

    fun updateGender(gender: Gender) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun saveAll() = viewModelScope.launch {
        withIOContext {
            val uiStateValue = _uiState.value
            val account = accountRepository.getAccount()
            val updatedAccount = account.copy(
                gender = uiStateValue.gender,
                birthDate = uiStateValue.dateOfBirth,
                userFitnessRecord = UserFitnessRecord(
                    height = uiStateValue.height?.let {
                        Length(
                            uiStateValue.heightUnit.toSI(it.toDouble())
                        )
                    },
                    weight = uiStateValue.weight?.let {
                        Mass(
                            uiStateValue.weightUnit.toSI(it.toDouble())
                        )
                    }
                )
            )
            accountRepository.updateAccount(updatedAccount)
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                return SignInViewModel(
                    accountRepository = application.asHarmonyApp().getAccountRepository()
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}