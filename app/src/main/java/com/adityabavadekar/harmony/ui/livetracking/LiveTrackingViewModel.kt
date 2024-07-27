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
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.data.model.WorkoutLap
import com.adityabavadekar.harmony.data.model.WorkoutLocation
import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.data.model.WorkoutRoute
import com.adityabavadekar.harmony.database.repo.WorkoutsRepository
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.utils.asHarmonyApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveTrackingViewModel(
    private val repository: WorkoutsRepository,
) : ViewModel() {

    private var _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted = _permissionsGranted.asStateFlow()

    private var startTimestamp: Long = 0L
    private var pauses = mutableListOf<WorkoutLap>()
    private var lastPauseTimestamp: Long? = null
    private var forcePause: Boolean = false
    private var pauseDetectorCounter = 0
    private var initialSteps: Int = 0
    private val workoutLocations: MutableList<WorkoutLocation> = mutableListOf()

    private var _uiState = MutableStateFlow<LiveTrackingUiState>(LiveTrackingUiState.nullState())
    val uiState: StateFlow<LiveTrackingUiState> = _uiState.asStateFlow()

    fun onLocationUpdated(location: Location) {
        val geoLocation = GeoLocation.from(location)
        var isPaused = false

        if (geoLocation.hasSpeed() && !forcePause) {
            if (geoLocation.requireSpeed.getSIValue() <= PAUSED_SPEED_THRESHOLD) {
                pauseDetectorCounter++
                if (pauseDetectorCounter >= PAUSED_SPEED_COUNTER_THRESHOLD) {
                    Log.w(TAG, "onLocationUpdated: DETECTED STALE (PAUSED) STATE")
                    isPaused = true
                }
            } else pauseDetectorCounter = 0
        }

        updateUiState { prevValue ->
            var workoutStatus = prevValue.workoutStatus
            if (isPaused) workoutStatus = LiveWorkoutStatus.PAUSED
            else {
                if (workoutStatus.isPaused() && !forcePause) {
                    // Activity was detected even though workout is paused.
                    // Thus, change workout status to Live
                    workoutStatus = LiveWorkoutStatus.LIVE
                }
            }

            prevValue.copy(
                locationCoordinates = if (workoutStatus.isLive()) geoLocation else prevValue.locationCoordinates,
                speed = if (workoutStatus.isLive() && geoLocation.hasSpeed()) geoLocation.requireSpeed else prevValue.speed,
                workoutStatus = workoutStatus
            )
        }
    }

    fun updateStepsCount(stepsSinceReboot: Int) {
        if (initialSteps == 0) initialSteps = stepsSinceReboot
        val newSteps = stepsSinceReboot - initialSteps
        initialSteps += newSteps
        Log.d(
            TAG,
            "updateStepsCount: newSteps=$newSteps stepsSinceReboot=$stepsSinceReboot initialSteps=$initialSteps"
        )
        updateUiState {
            it.copy(stepsCount = it.stepsCount + newSteps)
        }
    }

    private fun updateUiState(newUiState: (prevValue: LiveTrackingUiState) -> LiveTrackingUiState?) {
        newUiState(_uiState.value)?.let { _uiState.value = it }
    }

    fun onCountDownFinished() {
        startTimestamp = System.currentTimeMillis()
        updateUiState {
            it.copy(
                workoutStatus = LiveWorkoutStatus.LIVE,
                countDownFinished = true
            )
        }

        viewModelScope.launch {
            Log.d(TAG, "onCountDownFinished::while:TRUE [INITIALLY] ${_uiState.value}")

            while (_uiState.value.workoutStatus.isTrackable()) {

                /*
                * Update data only if workout is live and Skip if workout is Paused.
                * Since workout can be resumed again, the while loop should keep running until workout is completed.
                * */
                updateUiState { prevValue ->
                    if (prevValue.workoutStatus.isLive()) {
                        updatePauses()

                        return@updateUiState prevValue.copy(
                            liveTimeDifference = TimeDifference.now(
                                startTimestamp,
                                ignorablePauses = pauses
                            ),
                            distance = Length(prevValue.distance.getSIValue() + 2f)
                        )
                    }

                    if (lastPauseTimestamp == null) {
                        lastPauseTimestamp = System.currentTimeMillis()
                    }
                    return@updateUiState null
                }

                delay(UPDATE_INTERVAL_MILLIS)
            }
        }
    }

    private fun updatePauses() {
        if (lastPauseTimestamp != null) {
            pauses.add(
                WorkoutLap(
                    lastPauseTimestamp!!,
                    System.currentTimeMillis()
                )
            )
            lastPauseTimestamp = null
        }
    }

    fun pause() {
        forcePause = true
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.PAUSED)
        }
    }

    fun resume() {
        forcePause = false
        pauseDetectorCounter = 0
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.LIVE)
        }
    }

    fun complete() {
        forcePause = false
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.FINISHED)
        }
        updatePauses()
        viewModelScope.launch {
            repository.insertWorkoutRecord(createWorkoutRecord())
            Log.d(TAG, "complete: Workout Record Added")
        }
    }

    private fun createWorkoutRecord(): WorkoutRecord {
        val currentUiState = _uiState.value
        return WorkoutRecord(
            type = currentUiState.workoutType,
            title = "",
            description = "",
            startTimestamp = startTimestamp,
            endTimestamp = System.currentTimeMillis(),
            temperatureCelsius = null,
            distanceMeters = currentUiState.distance.getSIValue(),
            stepsCount = currentUiState.stepsCount,
            notes = null,
            laps = pauses,
            workoutRoute = null,
            totalEnergyBurnedCal = 0,
            minSpeedMetersSec = null,
            maxSpeedMetersSec = null,
            avgSpeedMetersSec = null,
            speedsMetersSec = listOf(currentUiState.speed.getSIValue()),
            completed = true
        )
    }

    fun setWorkoutType(type: WorkoutTypes) {
        updateUiState {
            it.copy(workoutType = type)
        }
    }

    fun allPermissionsGranted() {
        _permissionsGranted.value = true
    }

    companion object {
        private const val TAG = "[LiveTrackingViewModel]"
        private const val UPDATE_INTERVAL_MILLIS = 500L // 0.5sec
        private const val PAUSED_SPEED_THRESHOLD = 2f // m/s
        private const val PAUSED_SPEED_COUNTER_THRESHOLD = 15 // m/s

    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val application: Application,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = application.asHarmonyApp().getDatabase()
            return LiveTrackingViewModel(WorkoutsRepository.getInstance(db.workoutsDao())) as T
        }
    }

}
