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

package com.adityabavadekar.harmony.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LogoBack = Color.White

val WarningColorLight = Color(0xFFFFBB33)
val WarningColorDark = Color(0xFFFF8800)

val WorkoutPausedColorLight = Color(0xFFFFBB33)
val WorkoutPausedColorDark = Color(0xFFFF8800)

val LinkColor = Color(0xFF0000EE)

val colorPalate1 = listOf(
    Color(0xFFE3FDFD),
    Color(0xFFCBF1F5),
    Color(0xFFA6E3E9),
    Color(0xFF71C9CE),
)
val colorPalate2 = listOf(
    Color(0xFFF9F5F6),
    Color(0xFFF8E8EE),
    Color(0xFFFDCEDF),
    Color(0xFFF2BED1),
)

val CardColorElevation0Light = Color(0xFFCBF1F5)
val CardColorElevation0Dark = Color(0xFF222831)

@Preview
@Composable
private fun ColorsPrev() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxSize()
                    .background(CardColorElevation0Dark)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxSize()
                    .background(CardColorElevation0Light)
            )
        }
    }
}