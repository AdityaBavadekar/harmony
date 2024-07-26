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

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

enum class LottieAnimationSpeed(val speedValue: Float) {
    SPEED_0_5X(0.5f),
    SPEED_1X(1f),
    SPEED_1_5X(1.5f),
    SPEED_2X(2f),
    SPEED_3X(3f),
    SPEED_3_5X(3.5f),
    SPEED_4X(4f),
    SPEED_5X(5f),
}

@Composable
fun LotieLoader(
    @RawRes rawFileRes: Int,
    speed: LottieAnimationSpeed = LottieAnimationSpeed.SPEED_1X,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawFileRes))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = speed.speedValue,
        restartOnPlay = true
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        contentScale = ContentScale.Inside
    )
}