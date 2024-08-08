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

package com.adityabavadekar.harmony.ui.home

import com.adityabavadekar.harmony.data.model.TimeDifference


/**
 *
 *  StatsChartItem
 *  * Step Count of Today / Step Count goal (Circular Graph)
 *  * Total Calories today
 *  * Total workout hours
 *
 *  StepsCount
 *  * Step Count of Today / Step Count goal ( TEXT )
 *
 *  QuickNewWorkoutAccess(onClick = onAddNewClicked)
 *
 * DailyTarget
 *  * Daily goal completed how many times in this week? (x times of 7)
 *
 *  SleepTargetCount
 *  * Sleep duration of yesterday ( TEXT ) ( Information about it, was it at least required hrs?)
 *
 *  WaterIntakeCount
 *  * Water Intake Count of Today / Water Intake goal or Recommended ( TEXT )
 *
 *  WeightTarget
 *  * Graph
 *
 * */

data class HomeScreenUiState(
    val stepCountForTheDay: Int? = null,
    val stepCountGoal: Int? = null,
    val energyBurnedForTheDay: Double? = null,
    val energyBurnedGoal: Double? = null,
    val totalWorkoutDurationForDay: TimeDifference? = null,
    val sleepDuration: TimeDifference? = null,
    val waterIntakeCount: Int? = null,
    /**
     * Either a set goal, recommended value or a value calculated for the specific measurements of the user.
     * */
    val waterIntakeGoal: Int? = null,
    /**
     * Map of Weight (Double) to Timestamp (Long)
     * */
    val weightsGraph: Map<Double, Long>? = null,
    val ageInMonths: Int? = null,
)