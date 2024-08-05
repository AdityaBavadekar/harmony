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

import android.annotation.SuppressLint
import java.math.RoundingMode

@SuppressLint("DefaultLocale")
class NumberUtils {
    companion object {
        fun formatLong(number: Long): String = String.format("%,d", number)
        fun formatInt(number: Int): String = String.format("%,d", number)
        fun formatFloat(number: Float): String = String.format("%,.1f", number)

        fun formatDouble(num: Double): String = String.format("%,.2f", num)

        fun roundFloat(number: Float, decimalPlaces: Int = 1): Float =
            number.toBigDecimal().setScale(decimalPlaces, RoundingMode.UP).toFloat()

        fun roundDouble(number: Double, decimalPlaces: Int = 2): Double =
            number.toBigDecimal().setScale(decimalPlaces, RoundingMode.UP).toDouble()
    }
}