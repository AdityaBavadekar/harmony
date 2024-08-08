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

import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.Mass

/**
 * Stores Fitness related measurements of the user (eg: Weight, Height)
 * */
data class UserFitnessRecord(
    val stepsGoal: Int = DEFAULT_STEP_GOAL,
    val height: Length? = null,
    val weight: Mass? = null,
    val bedtimeSleep: Long? = null,
    val bedtimeWakeUp: Long? = null,
    val mostUsedWorkoutTypes: MostUsedWorkoutTypes = MostUsedWorkoutTypes(),
) {
    companion object {
        private const val DEFAULT_STEP_GOAL = 1500
    }
}