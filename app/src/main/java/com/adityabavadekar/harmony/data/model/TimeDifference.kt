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

import com.adityabavadekar.harmony.ui.common.TimeUnits

data class TimeDifference(
    val days: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    private val millisDifference: Long,
) {

    fun getValue(unit: TimeUnits): Double {
        val inSeconds = sumInSeconds()
        return when (unit) {
            TimeUnits.SECONDS -> inSeconds
            TimeUnits.MINUTES -> TimeUnits.MINUTES.fromSI(inSeconds)
            TimeUnits.HOURS -> TimeUnits.HOURS.fromSI(inSeconds)
            TimeUnits.DAYS -> TimeUnits.DAYS.fromSI(inSeconds)
        }
    }

    fun sumInSeconds(): Double {
        return TimeUnits.DAYS.toSI(days.toDouble()) +
                TimeUnits.HOURS.toSI(hours.toDouble()) +
                TimeUnits.MINUTES.toSI(minutes.toDouble()) +
                seconds
    }

    fun formatForDisplay(): String {
        fun formatInt(v: Int): String {
            val string = v.toString()
            if (string.length <= 1) return "0${string}"
            return string
        }

        val formattedString = "${formatInt(minutes)}:${formatInt(seconds)}"
        if (days == 0) {
            if (hours == 0) {
                return formattedString
            }
            //"H:mm:ss"
            return "${formatInt(hours)}:" + formattedString
        }

        //"d:H:mm:ss"
        return "${formatInt(days)}:${formatInt(hours)}:" + formattedString
    }

    companion object {
        fun zero(): TimeDifference {
            return TimeDifference(
                days = 0,
                hours = 0,
                minutes = 0,
                seconds = 0,
                millisDifference = 0
            )
        }

        fun from(startMillis: Long, endMillis: Long): TimeDifference {
            if (startMillis > endMillis) throw IllegalStateException("`startMillis` > `endMillis` (${startMillis} > ${endMillis})")
            return from(endMillis - startMillis)
        }

        fun now(startMillis: Long): TimeDifference {
            return from(startMillis, System.currentTimeMillis())
        }

        fun now(startMillis: Long, ignorablePauses: List<WorkoutLap>): TimeDifference {
            return from(
                startMillis,
                System.currentTimeMillis() - ignorablePauses.sumOf { it.diff() }
            )
        }

        fun from(interval: Long): TimeDifference {
            var timeDifference = interval
            val days = timeDifference / DAY_IN_MILLI
            timeDifference %= DAY_IN_MILLI
            val hours = timeDifference / HOUR_IN_MILLI
            timeDifference %= HOUR_IN_MILLI
            val minutes = timeDifference / MIN_IN_MILLI
            timeDifference %= MIN_IN_MILLI
            val seconds = timeDifference / SECOND_IN_MILLI
            timeDifference %= SECOND_IN_MILLI
            return TimeDifference(
                days = days.toInt(),
                hours = hours.toInt(),
                minutes = minutes.toInt(),
                seconds = seconds.toInt(),
                millisDifference = interval
            )
        }

        private const val SECOND_IN_MILLI = 1000
        private const val MIN_IN_MILLI = 60 * SECOND_IN_MILLI
        private const val HOUR_IN_MILLI = 60 * MIN_IN_MILLI
        private const val DAY_IN_MILLI = 24 * HOUR_IN_MILLI
    }
}