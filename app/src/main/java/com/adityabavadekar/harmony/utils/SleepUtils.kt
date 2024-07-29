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

class SleepUtils {

    enum class SleepType {
        RECOMMENDED,
        INSUFFICIENT,
        OVERSLEEPING
    }

    companion object {

        fun getSleepType(sleepHours: Int, ageInMonths: Int): SleepType {
            val sleepInfo = getSleepInfoForAge(ageInMonths)
            if (sleepHours <= sleepInfo.lowerThreshold) return SleepType.INSUFFICIENT
            if (sleepInfo.goodRange.contains(sleepHours)) return SleepType.RECOMMENDED
            return SleepType.OVERSLEEPING
        }

        data class SleepInfo(
            val goodRange: IntRange,
            val lowerThreshold: Int,
            val upperThreshold: Int
        )

        fun getSleepInfoForAge(ageInMonths: Int): SleepInfo {
            return when {
                ageInMonths <= 3 -> SleepInfo(
                    goodRange = 14..17,
                    lowerThreshold = 11,
                    upperThreshold = 19
                ) // Newborns (0-3 months)
                ageInMonths in 4..11 -> SleepInfo(
                    goodRange = 12..15,
                    lowerThreshold = 10,
                    upperThreshold = 18
                ) // Infants (4-11 months)
                ageInMonths in 12..23 -> SleepInfo(
                    goodRange = 11..14,
                    lowerThreshold = 9,
                    upperThreshold = 16
                ) // Toddlers (1-2 years)
                ageInMonths in 24..59 -> SleepInfo(
                    goodRange = 10..13,
                    lowerThreshold = 8,
                    upperThreshold = 14
                ) // Preschoolers (3-5 years)
                ageInMonths in 60..155 -> SleepInfo(
                    goodRange = 9..11,
                    lowerThreshold = 7,
                    upperThreshold = 12
                ) // School-aged children (6-13 years)
                ageInMonths in 156..203 -> SleepInfo(
                    goodRange = 8..10,
                    lowerThreshold = 7,
                    upperThreshold = 11
                ) // Teens (14-17 years)
                ageInMonths in 204..299 -> SleepInfo(
                    goodRange = 7..9,
                    lowerThreshold = 6,
                    upperThreshold = 11
                ) // Young adults (18-25 years)
                ageInMonths in 300..767 -> SleepInfo(
                    goodRange = 7..9,
                    lowerThreshold = 6,
                    upperThreshold = 10
                ) // Adults (26-64 years)
                else -> SleepInfo(
                    goodRange = 7..8,
                    lowerThreshold = 5,
                    upperThreshold = 9
                ) // Seniors (65 years and older)
            }
        }
    }
}