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

@Entity(tableName = "sleep_measurements_table")
data class SleepRecord(
    val startTimestamp: Long,
    val endTimestamp: Long
)

@Entity(tableName = "water_measurements_table")
data class WaterIntakeRecord(
    val timestamp: Long,
    val volumeInLitres: Double
)

@Entity(tableName = "heart_rate_measurements_table")
data class HeartRateRecord(
    val timestamp: Long,
    val rateBeatsPerMin: Int
)