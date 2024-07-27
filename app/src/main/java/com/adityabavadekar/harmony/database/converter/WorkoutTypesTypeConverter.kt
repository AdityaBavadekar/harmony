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

package com.adityabavadekar.harmony.database.converter

import androidx.room.TypeConverter
import com.adityabavadekar.harmony.data.WorkoutTypes

class WorkoutTypesTypeConverter {

    @TypeConverter
    fun fromWorkoutTypes(type: WorkoutTypes): String {
        return type.name
    }

    @TypeConverter
    fun toWorkoutTypes(name: String): WorkoutTypes {
        return WorkoutTypes.valueOf(name)
    }

}