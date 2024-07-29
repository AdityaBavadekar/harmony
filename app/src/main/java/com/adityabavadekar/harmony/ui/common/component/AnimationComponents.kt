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
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

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
//                start = Offset.Zero,
//                end = Offset.Infinite
            )
        }

    }
}