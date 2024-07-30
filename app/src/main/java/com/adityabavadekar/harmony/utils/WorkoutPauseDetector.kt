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

import android.util.Log
import com.adityabavadekar.harmony.ui.common.SpeedUnits

class WorkoutPauseDetector {

    private var pauseCounter: Int = 0

    /**
     * Resets the Pause Detection Counter.
     * */
    fun clear() {
        pauseCounter = 0
    }

    fun isPaused(speed: Double?, forcePauseByUser: Boolean): Boolean {
        if (forcePauseByUser) return true
        if (speed == null) return false

        if (speed < PAUSED_SPEED_THRESHOLD) {
            pauseCounter++
            if (pauseCounter >= PAUSED_COUNTER_THRESHOLD) {
                //Detected Paused State
                Log.d(TAG, "isPaused: TRUE \t[SPEED=$speed]")
                return true
            }
        } else clear()
        return false
    }

    companion object {
        private const val TAG = "[WorkoutPauseDetector]"
        private val PAUSED_SPEED_THRESHOLD = SpeedUnits.KILOMETERS_PER_HOUR.toSI(0.07)

        /**
         * Pause will be detected if the Pause Counter was incremented PAUSED_COUNTER_THRESHOLD times.
         * */
        private const val PAUSED_COUNTER_THRESHOLD = 10
    }

}