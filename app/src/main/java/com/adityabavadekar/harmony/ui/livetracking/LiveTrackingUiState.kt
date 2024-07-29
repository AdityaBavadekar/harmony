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

import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.ui.common.HeatUnits
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.Speed
import com.adityabavadekar.harmony.ui.common.SpeedUnits
import com.adityabavadekar.harmony.utils.UnitPreferences

data class LiveTrackingUiState(
    val workoutType: WorkoutTypes,
    val workoutStatus: LiveWorkoutStatus,
    val speed: Speed,
    val distance: Length,
    val stepsCount: Int,
    val liveTimeDifference: TimeDifference,
    val pausedTimeDifference: TimeDifference,
    val locationCoordinates: GeoLocation,
    val countDownFinished: Boolean,
    val speedUnit: SpeedUnits = UnitPreferences.getSpeedUnit(UnitPreferences.DEFAULT_DISTANCE_UNIT),
    val distanceUnit: LengthUnits = UnitPreferences.DEFAULT_DISTANCE_UNIT,
    val heatUnit: HeatUnits = UnitPreferences.DEFAULT_HEAT_UNIT,
) {
    companion object {
        fun nullState(): LiveTrackingUiState {

            return LiveTrackingUiState(
                workoutType = WorkoutTypes.TYPE_UNKNOWN,
                workoutStatus = LiveWorkoutStatus.NOT_STARTED,
                speed = Speed(0f),
                distance = Length(0f),
                stepsCount = 0,
                liveTimeDifference = TimeDifference.zero(),
                pausedTimeDifference = TimeDifference.zero(),
                locationCoordinates = GeoLocation.empty(),
                countDownFinished = false
            )
        }
    }
}