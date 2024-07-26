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

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.ui.common.Length
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveTrackingViewModel : ViewModel() {

    private var _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted = _permissionsGranted.asStateFlow()

    private var startTimestamp: Long = 0L
    private var distance: Length = Length(0f)
    private var pausedCounter = 0
    private var initialSteps: Int = 0
    private var _uiState = MutableStateFlow<LiveTrackingUiState>(LiveTrackingUiState.nullState())
    val uiState: StateFlow<LiveTrackingUiState> = _uiState.asStateFlow()


    fun setWorkoutType(type: WorkoutTypes) {
        updateUiState {
            it.copy(workoutType = type)
        }
    }

    fun onLocationUpdated(location: Location) {
        val pausedSpeedThreshold = 4f // m/s
        val pausedSpeedCounterThreshold = 8 // m/s
        val geoLocation = GeoLocation.from(location)
        var isPaused = false
        if (geoLocation.hasSpeed()) {
            if (geoLocation.requireSpeed.getSIValue() <= pausedSpeedThreshold) {
                pausedCounter++
                if (pausedCounter >= pausedSpeedCounterThreshold) {
                    Log.w(TAG, "onLocationUpdated: DETECTED STALE (PAUSED) STATE")
                    isPaused = true
                }
            } else pausedCounter = 0
        }
        updateUiState { prevValue ->
            var workoutStatus = prevValue.workoutStatus
            if (isPaused) workoutStatus = LiveWorkoutStatus.PAUSED
            else {
                if (workoutStatus.isPaused()) workoutStatus = LiveWorkoutStatus.LIVE
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
        _uiState.value = _uiState.value.copy(
            workoutStatus = LiveWorkoutStatus.LIVE,
            countDownFinished = true
        )
        viewModelScope.launch {
            Log.d(TAG, "onCountDownFinished::while:TRUE [INITIALLY] ${_uiState.value}")

            while (_uiState.value.workoutStatus.isTrackable()) {

                /*
                * Update data only if workout is live and Skip if workout is Paused.
                * Since workout can be resumed again, the while loop should keep running utill workout is completed.
                * */
                updateUiState { prevValue ->
                    if (prevValue.workoutStatus.isLive()) {
                        return@updateUiState prevValue.copy(
                            liveTimeDifference = TimeDifference.now(startTimestamp),
                            distance = Length(prevValue.distance.getSIValue() + 2f)
                        )
                    }
                    return@updateUiState null
                }

                delay(UPDATE_INTERVAL_MILLIS)
            }
        }
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
            it.copy(workoutStatus = LiveWorkoutStatus.FINISHED)
        }
    }

    fun allPermissionsGranted() {
        _permissionsGranted.value = true
    }

    companion object {
        private const val TAG = "[LiveTrackingViewModel]"
        private const val UPDATE_INTERVAL_MILLIS = 500L // 0.5sec
    }

}
