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

import android.location.Location
import android.util.Log
import com.adityabavadekar.harmony.data.model.WorkoutLocation
import com.adityabavadekar.harmony.data.model.WorkoutRoute

class WorkoutRouteManager {
    private val locations: MutableList<WorkoutLocation> = mutableListOf()

    fun addLocation(location: WorkoutLocation) {
        locations.add(location)
    }

    fun getDistanceTraversed(): Double {
        if (locations.size < 2) return 0.0
        val startLoc = locations.first()
        val endLoc = locations.last()
        /* Creates a new array of the specified size,
         * with all elements ((initialized to ZERO)). */
        val resultsArray = FloatArray(1)
        Location.distanceBetween(startLoc.lat, startLoc.long, endLoc.lat, endLoc.long, resultsArray)
        Log.i(
            "getDistanceTraversed",
            "getDistanceTraversed: (meters) [${resultsArray.joinToString()}]"
        )
        return resultsArray[0].toDouble()
    }


    // Use empty list for now to avoid [ConcurrentModificationException] thrown while Gson is decoding
    // TODO: Find solution.
    /**
     * Some possible solutions:
     * - Use a different table for storing location data as it takes time for loading it from json format
     * */
    fun route() = WorkoutRoute(emptyList())

}