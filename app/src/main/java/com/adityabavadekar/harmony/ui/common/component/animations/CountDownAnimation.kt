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

package com.adityabavadekar.harmony.ui.common.component.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import kotlinx.coroutines.delay

//TODO: Use 'Josefin Sans','Anton SC','Rajdhani'
@Composable
fun CountDownAnimation(
    modifier: Modifier = Modifier,
    startAt: Int = 5,
    delayMs: Long = 1000L,
    content: (@Composable (String) -> Unit)? = null,
    textSize: TextUnit = 98.sp,
    onCompleted: () -> Unit = {}
) {
    var textValue by remember { mutableIntStateOf(startAt) }

    LaunchedEffect(key1 = textValue) {
        while (textValue != 1) {
            delay(delayMs)
            textValue--
        }
        delay(delayMs)
        onCompleted()
    }

    for (i in textValue.toString().indices) {
        AnimatedContent(
            targetState = textValue.toString()[i],
            label = "countDown",
            transitionSpec = {
                slideInVertically { it } togetherWith slideOutVertically { it }
            }
        ) { c ->
            if (content != null) {
                content.invoke(c.toString())
            } else {
                Column(modifier = modifier) {
                    Row {
                        Text(
                            text = c.toString(),
                            fontSize = textSize,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CountDownAnimationPrev() {
    var hasCountDownFinished by remember {
        mutableStateOf(false)
    }
    HarmonyTheme {
        Surface {
            Column {
                when (hasCountDownFinished) {
                    false -> {
                        CountDownAnimation(Modifier.padding(18.dp)) { hasCountDownFinished = true }
                    }

                    true -> {
                        Text("Completed!!!!!!!!!")
                    }
                }
            }
        }
    }
}