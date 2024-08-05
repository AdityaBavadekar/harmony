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

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SimpleCalendar(timeInMillis: Long) {

    private val calendar = Calendar.getInstance()

    init {
        calendar.timeInMillis = timeInMillis
    }

    companion object {
        fun getInstance() = SimpleCalendar(System.currentTimeMillis())
        fun getInstance(timeInMillis: Long) = SimpleCalendar(timeInMillis)
    }

    fun isToday(): Boolean {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
    }

    fun isYesterday(): Boolean {
        val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }
        return yesterday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
    }

    fun isThisYear(): Boolean {
        return Calendar.getInstance().get(Calendar.YEAR) == year()
    }

    fun year(): Int {
        return calendar.get(Calendar.YEAR)
    }

    fun month(): Int {
        return calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
    }

    fun dayOfMonth(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun hourOfDay(): Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun minute(): Int {
        return calendar.get(Calendar.MINUTE)
    }

    fun second(): Int {
        return calendar.get(Calendar.SECOND)
    }

    fun dayOfWeek(): Int {
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getTimeInMillis() = calendar.timeInMillis

    fun format(pattern: String) =
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date(getTimeInMillis()))

}
