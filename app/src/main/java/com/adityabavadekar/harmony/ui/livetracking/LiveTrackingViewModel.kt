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
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.data.model.WorkoutLap
import com.adityabavadekar.harmony.data.model.WorkoutLocation
import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.database.repo.AccountRepository
import com.adityabavadekar.harmony.database.repo.WorkoutsRepository
import com.adityabavadekar.harmony.ui.common.HeatUnits
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.Speed
import com.adityabavadekar.harmony.ui.common.SpeedUnits
import com.adityabavadekar.harmony.ui.common.TimeUnits
import com.adityabavadekar.harmony.utils.CaloriesCounter
import com.adityabavadekar.harmony.utils.StepsCounter
import com.adityabavadekar.harmony.utils.WorkoutPauseDetector
import com.adityabavadekar.harmony.utils.WorkoutRouteManager
import com.adityabavadekar.harmony.utils.asHarmonyApp
import com.adityabavadekar.harmony.utils.withIOContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveTrackingViewModel(
    private val repository: WorkoutsRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private var _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted = _permissionsGranted.asStateFlow()

    private var startTimestamp: Long = 0L
    private var pauses = mutableListOf<WorkoutLap>()
    private var lastPauseTimestamp: Long? = null
    private var forcePausedByUser: Boolean = false
    private val workoutPauseDetector: WorkoutPauseDetector = WorkoutPauseDetector()
    private val workoutRouteManager: WorkoutRouteManager = WorkoutRouteManager()
    private val stepsCounter: StepsCounter = StepsCounter()
    private val caloriesCounter: CaloriesCounter = CaloriesCounter()
    private val speeds: MutableList<Double> = mutableListOf()
    private var userWeight: Double = 0.0
    private var captureWorker: ((name: String) -> Unit)? = null

    private var _uiState = MutableStateFlow(LiveTrackingUiState.nullState())
    val uiState: StateFlow<LiveTrackingUiState> = _uiState.asStateFlow()
    var recordId: Long? = null
        private set

    fun onLocationUpdated(location: Location) {
        val geoLocation = GeoLocation.from(location)

        if (geoLocation.isNullLocation()) return

        updateUiState { prevValue ->
            val previousWorkoutStatus = prevValue.workoutStatus
            if (!previousWorkoutStatus.isTrackable()) return@updateUiState prevValue

            var newWorkoutStatus: LiveWorkoutStatus = previousWorkoutStatus

            val isPauseDetected = workoutPauseDetector.isPaused(
                geoLocation.speedOrNull(),
                forcePausedByUser
            )
            if (isPauseDetected) {
                // Pause is detected, update the Workout Status to Paused
                newWorkoutStatus = LiveWorkoutStatus.PAUSED
            } else {
                // Pause is NOT detected
                // If Workout is not paused by the user but the previous workout status is equal to Paused
                // then change the status to Live as Pause is "NOT" detected.
                if (previousWorkoutStatus.isPaused() && !forcePausedByUser) {
                    // Activity was detected even though workout is auto paused.
                    // Thus, change workout status to Live
                    newWorkoutStatus = LiveWorkoutStatus.LIVE
                }
            }

            val speed: Speed? =
                if (newWorkoutStatus.isLive() && geoLocation.hasSpeed()) geoLocation.requireSpeed else null

            speed?.let { addNewSpeed(it) }

            if (!prevValue.locationCoordinates.isNullLocation()) {
                val locationStartTimestamp = prevValue.locationCoordinates.timestamp
                val locationEndTimestamp = System.currentTimeMillis()

                val newlyTravelledDistance = workoutRouteManager.addLocation(
                    WorkoutLocation.fromGeoLocation(
                        startTimestamp = locationStartTimestamp,
                        endTimestamp = locationEndTimestamp,
                        geoLocation = prevValue.locationCoordinates,
                    )
                )
                caloriesCounter.increment(
                    newDistanceMeters = newlyTravelledDistance,
                    durationSec = TimeDifference.from(
                        startMillis = locationStartTimestamp,
                        endMillis = locationEndTimestamp
                    ).getValue(TimeUnits.SECONDS)
                )
            }

            prevValue.copy(
                locationCoordinates = geoLocation,
                speed = speed ?: prevValue.speed,
                workoutStatus = newWorkoutStatus
            )
        }
    }

    fun updateStepsCount(stepsSinceReboot: Int) {
        stepsCounter.record(stepsSinceReboot)
        updateUiState {
            it.copy(stepsCount = stepsCounter.stepsCount())
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

        var databaseUpdateCounter = 0
        viewModelScope.launch {

            withIOContext {
                val recordId =
                    repository.insertWorkoutRecord(WorkoutRecord.startupRecord(type = _uiState.value.workoutType))
                this@LiveTrackingViewModel.recordId = recordId
                Log.i(TAG, "onCountDownFinished: [[[RECORD ID =$recordId]]]")
            }

            Log.d(TAG, "onCountDownFinished::while:TRUE [INITIALLY] ${_uiState.value}")

            while (_uiState.value.workoutStatus.isTrackable()) {

                val distanceTravelled = workoutRouteManager.getDistanceTraversed()
                val totalEnergyBurnedCal = getCalBurned()

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
                            /* TODO: Calories burnt */
                            distance = Length(distanceTravelled),
                            caloriesBurned = totalEnergyBurnedCal
                        )
                    }

                    if (lastPauseTimestamp == null) {
                        lastPauseTimestamp = System.currentTimeMillis()
                    }
                    return@updateUiState null
                }

                databaseUpdateCounter++

                if (databaseUpdateCounter >= 5) {
                    //Update database after every 5 sec
                    /**
                     * DATA:
                     * - pauses
                     * - route
                     * - speeds
                     * - distance
                     * - steps
                     * */
                    withIOContext {
                        val record = repository.getIncompleteWorkoutRecord()
                        if (record != null) {
                            repository.updateWorkoutRecord(
                                record.update(
                                    laps = pauses,
                                    workoutRoute = workoutRouteManager.route(),
                                    speeds = speeds,
                                    distanceMeters = distanceTravelled,
                                    stepsCount = stepsCounter.stepsCount(),
                                    totalEnergyBurnedCal = totalEnergyBurnedCal
                                )
                            )
                            speeds.clear()
                        }
                        databaseUpdateCounter = 0
                    }
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

    private fun addNewSpeed(speed: Speed) {
        speeds.add(speed.getSIValue())
    }

    fun pause() {
        forcePausedByUser = true
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.PAUSED)
        }
    }

    fun resume() {
        forcePausedByUser = false
        workoutPauseDetector.clear()
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.LIVE)
        }
    }

    fun complete() {
        forcePausedByUser = false
        updateUiState {
            it.copy(workoutStatus = LiveWorkoutStatus.FINISHED)
        }
        updatePauses()
        captureWorker?.invoke("w_map_" + requireNotNull(recordId).toString())
        viewModelScope.launch {
            withIOContext {
                val record = repository.getIncompleteWorkoutRecord()
                if (record != null) {
                    Log.d(TAG, "updating incomplete record: \n$record")
                    val updatedRecord = finalWorkoutRecord(record)
                    repository.updateWorkoutRecord(updatedRecord)
                    Log.d(TAG, "complete: Workout Record Added $updatedRecord")
                } else {
                    Log.e(TAG, "complete: record was null!!", NullPointerException())
                }
            }
        }
    }

    private fun finalWorkoutRecord(initialRecord: WorkoutRecord): WorkoutRecord {
        return initialRecord.updateAfterTrackingFinished(
            stepsCount = stepsCounter.stepsCount(),
            distanceMeters = workoutRouteManager.getDistanceTraversed(),
            totalEnergyBurnedCal = getCalBurned(),
            workoutRoute = workoutRouteManager.route(),
            laps = pauses,
            speeds = speeds,
            temperatureCelsius = null
        )
    }

    private fun getCalBurned(): Double {
        val c = caloriesCounter.getCount()
        Log.d(
            TAG,
            "getCalBurned: $c"
        )
        return c
    }

    fun setWorkoutType(type: WorkoutTypes) {
        updateUiState {
            it.copy(workoutType = type)
        }
    }

    fun allPermissionsGranted() {
        _permissionsGranted.value = true
    }

    fun setUserWeight(value: Double) {
        userWeight = value
        caloriesCounter.setUserWeight(userWeight)
    }

    fun setUnits(speedUnit: SpeedUnits, distanceUnit: LengthUnits, heatUnit: HeatUnits) {
        updateUiState { prevValue ->
            prevValue.copy(
                speedUnit = speedUnit,
                distanceUnit = distanceUnit,
                heatUnit = heatUnit
            )
        }
    }

    fun setCaptureWorker(worker: (name: String) -> Unit) {
        captureWorker = worker
    }

    init {
        viewModelScope.launch {
            withIOContext {
                val account = accountRepository.getAccount()
                account.userFitnessRecord?.weight?.let { userWeight = it.getSIValue().toDouble() }
            }
        }
    }

    companion object {
        private const val TAG = "[LiveTrackingViewModel]"
        private const val UPDATE_INTERVAL_MILLIS = 500L // 0.5sec
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val application: Application,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LiveTrackingViewModel(
                repository = application.asHarmonyApp().getWorkoutRepository(),
                accountRepository = application.asHarmonyApp().getAccountRepository()
            ) as T
        }
    }

}
