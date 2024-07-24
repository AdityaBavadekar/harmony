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

package com.adityabavadekar.harmony.data.model

import com.adityabavadekar.harmony.ui.common.Speed

data class WorkoutRecord(
    val type: String,
    val title: String,
    val description: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val temperatureCelsius: Float,
    val distanceMeters: Float,
    val notes: String? = null,
    val laps: List<WorkoutLap> = listOf(),
    val workoutRoute: WorkoutRoute? = null,
    val totalEnergyBurnedCal: Int? = null,
    val minSpeed: Speed? = null,
    val maxSpeed: Speed? = null,
    val avgSpeed: Speed? = null,
    val speeds: List<Speed> = listOf(),
    val additionalWorkoutInformation: Map<String, Any> = mapOf()
)