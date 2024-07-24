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

package com.adityabavadekar.harmony.ui.livetracking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.Speed
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.component.animations.CountDownAnimation
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.NumberUtils

@Composable
fun MapView(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
    ) {
    }
}


@Composable
fun LiveTrackingSpaceEvenRow(
    modifier: Modifier = Modifier,
    content: @Composable() (RowScope.() -> Unit)
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        content()
    }
}

@Composable
fun LiveTrackingActivityLabel(
    modifier: Modifier = Modifier,
    activityLabel: String = "Cycling",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier.alpha(0.9f),
            text = activityLabel,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun LiveTrackingBottomButtons(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LiveTrackingButton(
            text = "Pause",
            modifier = Modifier.weight(1f),
        )
        HorizontalSpacer(size = 18.dp)
        LiveTrackingButton(
            text = "Finish",
            modifier = Modifier.weight(1f),
            outlined = true
        )
    }
}

@Composable
fun LiveTrackingButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    outlined: Boolean = false
) {
    val paddingValues = PaddingValues(vertical = 18.dp, horizontal = 32.dp)
    if (outlined) {
        OutlinedButton(
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = paddingValues,
            onClick = onClick
        ) {
            Text(text = text)
        }
    } else {
        Button(
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = paddingValues,
            onClick = onClick
        ) {
            Text(text = text)
        }
    }
}

@Composable
fun LiveTrackingTelemetryItem(
    modifier: Modifier = Modifier,
    telemetry: Int = 14,
    telemetryUnit: String? = null,
    telemetryLabel: String = "Steps",
) {
    Column(
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.alpha(0.8f),
                    text = NumberUtils.formatNumber(telemetry.toLong()),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                VerticalSpacer()
                Text(
                    modifier = Modifier.alpha(0.9f),
                    text = telemetryUnit ?: "",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    modifier = Modifier.alpha(0.9f),
                    text = telemetryLabel,
                )
            }
        }
    }
}

enum class WorkoutStatus { PAUSED, LIVE, FINISHED; }

@Composable
fun LiveTrackingTime(
    modifier: Modifier = Modifier,
    time: String = "02:15",
    workoutStatus: WorkoutStatus = WorkoutStatus.LIVE,
    statusText: String? = null
) {
    val statusTextStyle: TextStyle
    val timeTextStyle: TextStyle
    val contentColor: Color

    when (workoutStatus) {
        WorkoutStatus.LIVE -> {
            contentColor = MaterialTheme.colorScheme.primary
            statusTextStyle = MaterialTheme.typography.bodyLarge
            timeTextStyle = MaterialTheme.typography.displayLarge
        }

        else -> {
            contentColor = MaterialTheme.colorScheme.tertiary
            statusTextStyle = MaterialTheme.typography.displayLarge
            timeTextStyle = MaterialTheme.typography.displaySmall
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            contentColor = contentColor,
        ) {
            Column(
                modifier = Modifier
                    .height(180.dp)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                statusText?.let {
                    Text(
                        modifier = Modifier.alpha(0.9f),
                        text = it,
                        style = statusTextStyle,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = time,
                    style = timeTextStyle,
                    fontWeight = FontWeight.Bold,
                )
                VerticalSpacer()
                Text(text = "Time")

            }
        }
    }
}


@Composable
fun LiveTrackerBox(modifier: Modifier = Modifier) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Surface {
            Column(
                Modifier.padding(18.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                LiveTrackingActivityLabel()
                LiveTrackingTime(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                )
                LiveTrackingSpaceEvenRow {
                    LiveTrackingTelemetryItem(
                        telemetry = 400,
                        telemetryUnit = "km",
                        telemetryLabel = "Distance"
                    )
                    LiveTrackingTelemetryItem(
                        telemetry = 240000,
                        telemetryLabel = "Steps"
                    )
                    LiveTrackingTelemetryItem(
                        telemetry = 1500,
                        telemetryUnit = "km/hr",
                        telemetryLabel = "Speed"
                    )
                }
                VerticalSpacer(size = 32.dp)
                LiveTrackingBottomButtons()
            }
        }
    }
}

@Composable
fun LiveTrackingScreen() {
    Column(
        Modifier.fillMaxSize()
    ) {
        MapView(Modifier.weight(1f))
        LiveTrackerBox()
    }
}

@Composable
fun LiveTrackingDeciderScreen() {
    var hasCountDownFinished by remember {
        mutableStateOf(false)
    }
    when (hasCountDownFinished) {
        false -> LiveTrackingCountDownScreen { hasCountDownFinished = true }
        true -> LiveTrackingScreen()
    }
}

@Composable
fun LiveTrackingCountDownScreen(
    onCompleted: () -> Unit = {}
) {
    Surface {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CountDownAnimation(
                startAt = 3,
                onCompleted = onCompleted
            )
        }
    }
}

data class LiveTrackingTelemetry(
    val status: WorkoutStatus,
    val passedTimeText: String,
    val distance: Length,
    val stepsCount: Int,
    val speed: Speed,
)

@Preview(name = "Before", group = "Live")
@Composable
private fun LiveTrackingBeforeScreenPrev() {
    HarmonyTheme {
        Surface {
            LiveTrackingDeciderScreen()
        }
    }
}
@Preview(name = "After")
@Composable
private fun LiveTrackingAfterScreenPrev() {
    HarmonyTheme {
        Surface {
            LiveTrackingScreen()
        }
    }
}