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

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

class AnimationComponents {
    companion object {

        @Composable
        fun gradientAnimation(modifier: Modifier = Modifier): Brush {
            val primaryContainer = MaterialTheme.colorScheme.primary
            val gradientColorsList = listOf(
                listOf(Color.Blue, Color.Yellow),
                listOf(Color.Yellow, Color.Blue),
            )
            val duration = 1000
            val infiniteTransition = rememberInfiniteTransition(label = "text-color-sweep")
            val color1 by infiniteTransition.animateColor(
                initialValue = gradientColorsList.first()[0],
                targetValue = gradientColorsList.last()[0],
                animationSpec = infiniteRepeatable(
                    animation = tween(duration, easing = EaseIn),
                    repeatMode = RepeatMode.Reverse
                ), label = "color1"
            )
            val color2 by infiniteTransition.animateColor(
                initialValue = gradientColorsList.first()[1],
                targetValue = gradientColorsList.last()[1],
                animationSpec = infiniteRepeatable(
                    animation = tween(duration, easing = EaseOut),
                    repeatMode = RepeatMode.Reverse
                ), label = "color2"
            )
            val alpha = 0.3f
            return Brush.linearGradient(
                colors = listOf(color1.copy(alpha = alpha), color2.copy(alpha = alpha + 0.2f)),
            )
        }

        @Composable
        fun LoadingArcAnimation() {
            val brush =
                Brush.verticalGradient(listOf(Color.Blue, Color.Yellow, Color.Red))
            val strokeWidth = 15f
            val loadingBoxSize = 100.dp
            val infiniteTransition = rememberInfiniteTransition(label = "")
            val animationValue by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    tween(
                        durationMillis = 1200,
                        easing = EaseInOutCubic//EaseInOutCubic
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "",
            )
            val endAngle =
                infiniteTransition.animateFloat(
                    180f,
                    0f,
                    infiniteRepeatable(
                        animation = tween(1000, easing = EaseInOutSine),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )

            val startAngle =
                infiniteTransition.animateFloat(
                    0f,
                    120f,
                    infiniteRepeatable(
                        animation = tween(900, easing = EaseInOutSine),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(loadingBoxSize)
                        .drawBehind {
                            rotate(animationValue) {
                                drawArc(
                                    brush = brush,
                                    startAngle = endAngle.value,
                                    sweepAngle = animationValue + startAngle.value,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                            }
                        }
                )
            }
        }
    }


}
