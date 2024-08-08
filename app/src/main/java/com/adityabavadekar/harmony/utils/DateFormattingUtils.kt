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

class DateFormattingUtils {
    companion object {
        fun formatWorkoutSummaryDate(startTimestamp: Long): String {
            val target = SimpleCalendar.getInstance(startTimestamp)

            var pattern: String = FORMAT_FULL_NO_YEAR
            var prefix = ""
            if (target.isToday()) {
                pattern = FORMAT_TIME_ONLY
                prefix = "Today at"
            } else if (target.isYesterday()) {
                pattern = FORMAT_TIME_ONLY
                prefix = "Yesterday at"
            } else if (target.isThisYear()) {
                pattern = FORMAT_FULL_NO_YEAR
            }

            return prefix + " " + target.format(pattern)
        }

        private const val FORMAT_FULL_WITH_YEAR = "dd MMMM, h:mm a"
        private const val FORMAT_FULL_NO_YEAR = "dd MMMM yyyy, h:mm a"
        private const val FORMAT_TIME_ONLY = "h:mm a"
    }
}