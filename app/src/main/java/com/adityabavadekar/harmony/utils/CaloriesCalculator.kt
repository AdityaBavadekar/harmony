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

package com.adityabavadekar.harmony.utils

import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.ui.common.HeatUnits

class CaloriesCalculator {
    companion object {
        /**
         * Calculates the calories burned based on the duration of the activity, MET value, and weight of the person.
         * This calculation is independent of the distance traveled.
         *
         * @param workoutType The type of Workout.
         * @param durationSeconds The duration of the activity in seconds.
         * @param weightKg The weight of the person in kilograms.
         * @return The number of calories burned.
         */
        fun calculateCaloriesBurnedMET(
            workoutType: WorkoutTypes,
            durationSeconds: Double,
            weightKg: Double,
        ): Double {
            return durationSeconds * workoutType.metValue * weightKg / 60 * 200
        }

        /**
         * Calculates the calories burned.
         *
         * @return The number of calories burned.
         */
        fun calculateCaloriesBurned(
            workoutType: WorkoutTypes,
            durationSeconds: Double,
            distanceInMeters: Double,
            weightKg: Double,
        ): Double {
            return HeatUnits.CALORIES.fromSI((9.8 * distanceInMeters) / durationSeconds)
        }
    }
}