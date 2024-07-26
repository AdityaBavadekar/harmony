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

data class TimeDifference(
    val days: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
) {

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
        } else {
            //"H:mm:ss"
            return "${formatInt(days)}:" + formattedString
        }
    }

    companion object {
        fun zero(): TimeDifference {
            return TimeDifference(days = 0, hours = 0, minutes = 0, seconds = 0)
        }

        fun from(startMillis: Long, endMillis: Long): TimeDifference {
            if (startMillis > endMillis) throw IllegalStateException("`startMillis` > `endMillis`")
            return from(endMillis - startMillis)
        }

        fun now(startMillis: Long): TimeDifference {
            return from(startMillis, System.currentTimeMillis())
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
                seconds = seconds.toInt()
            )
        }

        private const val SECOND_IN_MILLI = 1000
        private const val MIN_IN_MILLI = 60 * SECOND_IN_MILLI
        private const val HOUR_IN_MILLI = 60 * MIN_IN_MILLI
        private const val DAY_IN_MILLI = 24 * HOUR_IN_MILLI
    }
}