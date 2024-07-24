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

import com.adityabavadekar.harmony.data.WorkoutTypes

/**
 * Stores 4 workout types that are used multiple by the user
 */
data class MostUsedWorkoutTypes(
    val mostUsed1: WorkoutTypes,
    val mostUsed2: WorkoutTypes,
    val mostUsed3: WorkoutTypes,
    val mostUsed4: WorkoutTypes,
)
