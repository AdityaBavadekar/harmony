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

package com.adityabavadekar.harmony.ui.livetracking

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.database.repo.AccountRepository
import com.adityabavadekar.harmony.database.repo.WorkoutsRepository
import com.adityabavadekar.harmony.ui.common.Speed
import com.adityabavadekar.harmony.utils.LiveRecordId
import com.adityabavadekar.harmony.utils.UnitPreferences
import com.adityabavadekar.harmony.utils.asHarmonyApp
import com.adityabavadekar.harmony.utils.withIOContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveTrackingViewModelV2(
    private val repository: WorkoutsRepository,
    private val accountRepository: AccountRepository,
    private val liveRecordId: LiveRecordId
) : ViewModel() {

    private var _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted = _permissionsGranted.asStateFlow()

    private var _uiState = MutableStateFlow(LiveTrackingUiState.nullState())
    val uiState: StateFlow<LiveTrackingUiState> = _uiState.asStateFlow()
    var recordId: Long? = null

    private fun updateUiState(newUiState: (prevValue: LiveTrackingUiState) -> LiveTrackingUiState?) {
        newUiState(_uiState.value)?.let { _uiState.value = it }
    }

    fun pause() {
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.PAUSED)
        }
    }

    fun resume() {
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.LIVE)
        }
    }

    fun complete() {
        updateUiState {
            it.copy(
                workoutStatus = LiveWorkoutStatus.FINISHED,
                showLoading = true
            )
        }
    }

    fun setWorkoutType(type: WorkoutTypes) {
        updateUiState {
            it.copy(workoutType = type)
        }
    }

    fun allPermissionsGranted() {
        _permissionsGranted.value = true
    }

    fun startListening() = viewModelScope.launch {
        initRecordId()
        withIOContext {
            while (recordId == null) {
                Log.d(TAG, "startListening: Record id is null")
                delay(100)
                initRecordId()
            }
            updateUiState { it.copy(countDownFinished = true) }
            repository.listenToRecord(recordId!!).collect { record ->
                record?.let {
                    updateUiState { prevValue: LiveTrackingUiState ->
                        prevValue.copy(
                            workoutType = record.type,
                            distance = record.distance(),
                            stepsCount = record.stepsCount,
                            caloriesBurned = record.totalEnergyBurnedJoules,
                            liveTimeDifference = TimeDifference.now(
                                record.startTimestamp,
                                ignorablePauses = record.laps
                            ),
                            //pausedTimeDifference: TimeDifference,
                            //locationCoordinates = record
                        )
                    }
                }
            }
        }
    }

    private fun initRecordId() {
        if (recordId != null) return

        liveRecordId.getId()?.let {
            recordId = it
            Log.d(TAG, "initRecordId: Record id initialised")
        }
        if (recordId == null) {
            Log.d(TAG, "initRecordId: Record id was null")
        }
    }

    init {
        initRecordId()
    }

    fun setUnits(unitPreferences: UnitPreferences.PhysicalUnitPreferences) {
        updateUiState {
            it.copy(
                distanceUnit = unitPreferences.distanceUnit,
                speedUnit = unitPreferences.speedUnit(),
                heatUnit = unitPreferences.heatUnit
            )
        }
    }

    fun updateStatus(workoutStatus: String) {
        updateUiState { it.copy(workoutStatus = LiveWorkoutStatus.valueOf(workoutStatus)) }
    }

    fun updateLocation(location: GeoLocation) {
        updateUiState {
            it.copy(
                locationCoordinates = location,
                speed = location.speedOrNull()?.let { v -> Speed(v) } ?: Speed(0.0)
            )
        }
    }

    init {
        val lastWorkoutRecordId = liveRecordId.getId()
        if (lastWorkoutRecordId != null) {
            // Live tracking activity has been started after
            // user navigated to home screen and chose to go
            // back to currently active workout session.
            // In this case count-down should not be shown.
            recordId = lastWorkoutRecordId
            updateUiState { it.copy(countDownFinished = true) }
            Log.i(TAG, "init: CONTINUING FROM LAST WORKOUT STATE.")
        }
    }

    companion object {
        private const val TAG = "[LiveTrackingViewModelV2]"
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val application: Application,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LiveTrackingViewModelV2(
                repository = application.asHarmonyApp().getWorkoutRepository(),
                accountRepository = application.asHarmonyApp().getAccountRepository(),
                liveRecordId = LiveRecordId(application.applicationContext)
            ) as T
        }
    }

}
