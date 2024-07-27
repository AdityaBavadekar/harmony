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

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adityabavadekar.harmony.data.WorkoutTypes

@Entity(tableName = "workouts_table")
data class WorkoutRecord(
    val type: WorkoutTypes,
    val title: String,
    val description: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val temperatureCelsius: Float? = null,
    val distanceMeters: Float,
    val stepsCount: Int,
    val notes: String? = null,
    val laps: List<WorkoutLap> = listOf(),
    val workoutRoute: WorkoutRoute? = null,
    val totalEnergyBurnedCal: Int? = null,
    val minSpeedMetersSec: Float? = null,
    val maxSpeedMetersSec: Float? = null,
    val avgSpeedMetersSec: Float? = null,
    val speedsMetersSec: List<Float> = listOf(),
    val completed: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun timeDifference() =
        TimeDifference.from(startTimestamp, endTimestamp - laps.sumOf { it.diff() })

    companion object {
        fun simple(
            type: WorkoutTypes,
            startTimestamp: Long,
            endTimestamp: Long,
            distanceMeters: Float,
            stepsCount: Int,
            totalEnergyBurnedCal: Int = 0,
            avgSpeedMetersSec: Float? = null,
            maxSpeedMetersSec: Float? = null,
            minSpeedMetersSec: Float? = null,
        ): WorkoutRecord {
            return WorkoutRecord(
                type = type,
                title = "",
                description = "",
                startTimestamp = startTimestamp,
                endTimestamp = endTimestamp,
                temperatureCelsius = null,
                distanceMeters = distanceMeters,
                stepsCount = stepsCount,
                notes = null,
                laps = listOf(),
                workoutRoute = null,
                totalEnergyBurnedCal = totalEnergyBurnedCal,
                minSpeedMetersSec = minSpeedMetersSec,
                maxSpeedMetersSec = maxSpeedMetersSec,
                avgSpeedMetersSec = avgSpeedMetersSec,
                speedsMetersSec = listOf(),
                completed = true
            )
        }
    }

}