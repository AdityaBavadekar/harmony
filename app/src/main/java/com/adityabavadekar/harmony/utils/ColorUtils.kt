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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.theme.CardColorElevation0Dark
import com.adityabavadekar.harmony.ui.theme.CardColorElevation0Light
import com.adityabavadekar.harmony.ui.theme.WarningColorDark
import com.adityabavadekar.harmony.ui.theme.WarningColorLight
import com.adityabavadekar.harmony.ui.theme.WorkoutActiveDark
import com.adityabavadekar.harmony.ui.theme.WorkoutActiveLight

class ColorUtils {
    companion object {
        @Composable
        fun getWarningColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
            return if (darkTheme) WarningColorDark else WarningColorLight
        }

        @Composable
        fun getCardColor(elevation: Dp = 0.dp, darkTheme: Boolean = isSystemInDarkTheme()): Color {
            return if (darkTheme) CardColorElevation0Dark else CardColorElevation0Light
        }

        @Composable
        fun getActiveWorkoutListItemColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
            return if (darkTheme) WorkoutActiveDark else WorkoutActiveLight
        }

        fun getActiveWorkoutNotificationColor(): Int {
            return WorkoutActiveLight.toArgb()
        }


    }
}