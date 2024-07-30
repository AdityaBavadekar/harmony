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

import androidx.room.DatabaseView
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.ui.common.Length

/**
 * Class that contains selected fields from [WorkoutRecord], required to show a list of Workouts.
 * Also defines functions on this data, to help in showing the information to user.
 * */
@DatabaseView(
    "SELECT workouts_table.id, workouts_table.type, workouts_table.title, " +
            "workouts_table.startTimestamp, workouts_table.endTimestamp, " +
            "workouts_table.distanceMeters, workouts_table.stepsCount, " +
            "workouts_table.totalEnergyBurnedJoules, workouts_table.pauseDurationMillis " +
            "FROM workouts_table"
)
data class WorkoutSummaryRecord(
    val id: Long,
    val type: WorkoutTypes,
    val title: String? = null,
    val startTimestamp: Long,
    val endTimestamp: Long = 0L,
    val distanceMeters: Double = 0.0,
    val stepsCount: Int = 0,
    val pauseDurationMillis: Long = 0L,
    val totalEnergyBurnedCal: Double? = null,
) {

    fun timeDifference() =
        TimeDifference.from(startTimestamp, endTimestamp + pauseDurationMillis)

    fun getDisplayDistance() {
        Length(lengthInMeters = distanceMeters)
    }

}