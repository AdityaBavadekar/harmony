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

package com.adityabavadekar.harmony.ui.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

@Composable
fun RingChart(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    strokeWidth: Float = 50f,
    placeholderTrackColors: List<Color> = listOf(
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
    ),
    completedTrackColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 1f),
        MaterialTheme.colorScheme.primary.copy(alpha = 1f),
    ),
    startAngle: Float = 0f,
    endAngle: Float = -90f,
    content: (@Composable BoxScope.() -> Unit) = {},
) {
    val paddingSize: Dp = 0.dp
    val placeholderTrackBrush =
        Brush.linearGradient(colors = placeholderTrackColors, tileMode = TileMode.Repeated)
    val completedTrackBrush =
        Brush.linearGradient(colors = completedTrackColors, tileMode = TileMode.Repeated)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularArc(
            size = size,
            paddingSize = paddingSize,
            brush = placeholderTrackBrush,
            strokeWidth = strokeWidth,
            startAngle = 0f,
            endAngle = 360f
        )

        CircularArc(
            size = size,
            paddingSize = paddingSize,
            brush = completedTrackBrush,
            strokeWidth = strokeWidth,
            startAngle = startAngle,
            endAngle = endAngle
        )

        content()
    }
}

@Composable
fun CircularArc(
    size: Dp,
    paddingSize: Dp,
    brush: Brush,
    strokeWidth: Float,
    startAngle: Float,
    endAngle: Float,
) {

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .padding(paddingSize)
            .size(size)
    ) {
        drawArc(
            brush = brush,
            startAngle = startAngle,
            sweepAngle = endAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

class DiskChartPositions {
    companion object {
        const val TOP = 270f
        const val BOTTOM = 90f
        const val LEFT = 180f
        const val RIGHT = 0f
    }
}

fun convertPercentageToAngle(percentage: Float): Float {
    return (percentage * 360f) / 100
}

@Preview
@Composable
private fun DiskChartPrev() {
    HarmonyTheme {
        Surface {
            RingChart(
                modifier = Modifier.padding(28.dp),
                strokeWidth = 40f,
                startAngle = DiskChartPositions.BOTTOM,
                endAngle = convertPercentageToAngle(50f),
            ) {
                Text(
                    text = "50 KM",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}