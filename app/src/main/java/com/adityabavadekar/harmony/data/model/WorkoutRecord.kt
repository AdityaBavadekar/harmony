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
    val title: String? = null,
    val description: String? = null,
    val startTimestamp: Long,
    val endTimestamp: Long = 0L,
    val temperatureCelsius: Double? = null,
    val distanceMeters: Double = 0.0,
    val stepsCount: Int = 0,
    val notes: String? = null,
    val laps: List<WorkoutLap> = listOf(),
    val pauseDurationSec: Long = 0L,
    val workoutRoute: WorkoutRoute? = null,
    val totalEnergyBurnedCal: Double? = null,
    val minSpeedMetersSec: Double? = null,
    val maxSpeedMetersSec: Double? = null,
    val avgSpeedMetersSec: Double? = null,
    val speedsMetersSec: List<Double> = listOf(),
    val completed: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) {

    fun timeDifference() =
        TimeDifference.from(startTimestamp, endTimestamp - laps.sumOf { it.diff() })

    fun updateAfterTrackingFinished(
        stepsCount: Int,
        distanceMeters: Double,
        totalEnergyBurnedCal: Double,
        workoutRoute: WorkoutRoute,
        laps: List<WorkoutLap>,
        speeds: List<Double>,
        temperatureCelsius: Double? = null
    ): WorkoutRecord {
        val finalSpeeds = speedsMetersSec + speeds
        return this.copy(
            endTimestamp = System.currentTimeMillis(),
            temperatureCelsius = temperatureCelsius,
            distanceMeters = distanceMeters,
            stepsCount = stepsCount,
            laps = laps,
            workoutRoute = workoutRoute,
            totalEnergyBurnedCal = totalEnergyBurnedCal,
            minSpeedMetersSec = finalSpeeds.minOrNull(),
            maxSpeedMetersSec = finalSpeeds.maxOrNull(),
            avgSpeedMetersSec = finalSpeeds.average(),
            speedsMetersSec = finalSpeeds,
            completed = true
        )
    }

    fun update(
        stepsCount: Int,
        distanceMeters: Double,
        totalEnergyBurnedCal: Double,
        workoutRoute: WorkoutRoute,
        laps: List<WorkoutLap>,
        speeds: List<Double>,
        temperatureCelsius: Double? = null
    ): WorkoutRecord {
        val finalSpeeds = speedsMetersSec + speeds
        return this.copy(
            temperatureCelsius = temperatureCelsius,
            distanceMeters = distanceMeters,
            stepsCount = stepsCount,
            laps = laps,
            workoutRoute = workoutRoute,
            totalEnergyBurnedCal = totalEnergyBurnedCal,
            minSpeedMetersSec = finalSpeeds.minOrNull(),
            maxSpeedMetersSec = finalSpeeds.maxOrNull(),
            avgSpeedMetersSec = finalSpeeds.average(),
            speedsMetersSec = finalSpeeds,
        )
    }


    companion object {
        private fun simple(
            type: WorkoutTypes,
            startTimestamp: Long,
            endTimestamp: Long = 0L,
            distanceMeters: Double = 0.0,
            stepsCount: Int = 0,
            totalEnergyBurnedCal: Double = 0.0,
            avgSpeedMetersSec: Double? = null,
            maxSpeedMetersSec: Double? = null,
            minSpeedMetersSec: Double? = null,
            completed: Boolean,
        ): WorkoutRecord {
            return WorkoutRecord(
                type = type,
                title = null,
                description = null,
                startTimestamp = startTimestamp,
                endTimestamp = endTimestamp,
                temperatureCelsius = null,
                distanceMeters = distanceMeters,
                stepsCount = stepsCount,
                notes = null,
                laps = listOf(),
                pauseDurationSec = 0L,
                workoutRoute = null,
                totalEnergyBurnedCal = totalEnergyBurnedCal,
                minSpeedMetersSec = minSpeedMetersSec,
                maxSpeedMetersSec = maxSpeedMetersSec,
                avgSpeedMetersSec = avgSpeedMetersSec,
                speedsMetersSec = listOf(),
                completed = completed
            )
        }

        fun startupRecord(type: WorkoutTypes): WorkoutRecord {
            return simple(
                type = type,
                startTimestamp = System.currentTimeMillis(),
                completed = false /* Most important */
            )
        }
    }

}