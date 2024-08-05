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

package com.adityabavadekar.harmony.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.data.model.UserRecord
import com.adityabavadekar.harmony.data.model.WorkoutSummaryRecord
import com.adityabavadekar.harmony.database.repo.AccountRepository
import com.adityabavadekar.harmony.database.repo.WorkoutsRepository
import com.adityabavadekar.harmony.ui.home.HomeScreenUiState
import com.adityabavadekar.harmony.utils.asHarmonyApp
import com.adityabavadekar.harmony.utils.withIOContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val accountRepository: AccountRepository,
    private val workoutRepository: WorkoutsRepository
) : ViewModel() {

    private var _workouts: MutableStateFlow<List<WorkoutSummaryRecord>> =
        MutableStateFlow(emptyList())
    val workouts: StateFlow<List<WorkoutSummaryRecord>> = _workouts.asStateFlow()

    private var _account: MutableStateFlow<UserRecord?> = MutableStateFlow(null)
    val account: StateFlow<UserRecord?> = _account.asStateFlow()

    private var _homeScreenUiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    private fun loadWorkouts() = viewModelScope.launch {
        withIOContext {
            workoutRepository.getAllSummaryRecords().collect {
                _workouts.value = it
                recalculateStats(it)
            }
        }
    }

    private fun recalculateStats(workoutSummaryRecords: List<WorkoutSummaryRecord>) {
        var totalStepCount = 0
        var totalEnergyCount = 0.0
        var totalWorkoutDuration = TimeDifference.zero()
        for (record in workoutSummaryRecords) {
            totalStepCount += record.stepsCount
            record.totalEnergyBurnedJoules?.let { totalEnergyCount += it }
            totalWorkoutDuration += record.timeDifference()
        }
        _homeScreenUiState.value = _homeScreenUiState.value.copy(
            stepCountForTheDay = totalStepCount,
            energyBurnedForTheDay = totalEnergyCount,
            totalWorkoutDurationForDay = totalWorkoutDuration
        )
    }

    private fun loadAccount() = viewModelScope.launch {
        withIOContext {
            accountRepository.getAccount().let {
                _account.value = it
                _homeScreenUiState.value = _homeScreenUiState.value.copy(
                    stepCountGoal = it.userFitnessRecord?.stepsGoal,
                )
            }
        }
    }

    init {
        loadWorkouts()
        loadAccount()
    }

    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                val app = application.asHarmonyApp()
                return MainActivityViewModel(
                    accountRepository = app.getAccountRepository(),
                    workoutRepository = app.getWorkoutRepository()
                ) as T
            }
            throw IllegalArgumentException()
        }
    }

}