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

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.Speed
import com.adityabavadekar.harmony.ui.common.component.ComposeMapView
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.LotieLoader
import com.adityabavadekar.harmony.ui.common.component.LottieAnimationSpeed
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.component.animations.CountDownAnimation
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.NumberUtils
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.math.RoundingMode

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    locationCoordinates: GeoLocation,
) {
    Column(
        modifier
            .fillMaxWidth()
    ) {
        ComposeMapView(
            point = locationCoordinates,
            disableTouchControls = true
        ) {
            //TODO
            //it.onPause()
            //it.onDetach()
            //it.onResume()
        }
    }
}


@Composable
fun LiveTrackingSpaceEvenRow(
    modifier: Modifier = Modifier,
    content: @Composable() (RowScope.() -> Unit),
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
    status: LiveWorkoutStatus,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .alpha(0.9f)
                .align(Alignment.CenterVertically),
            text = activityLabel,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        HorizontalSpacer(size = 8.dp)
        Box(
            Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            if (status.isLive()) {
                LotieLoader(rawFileRes = R.raw.live_anim, speed = LottieAnimationSpeed.SPEED_2X)
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    shape = CircleShape
                ) {
                    Box(
                        Modifier.size(18.dp),
                        content = {}
                    )
                }
            }
        }
    }
}

@Composable
fun LiveTrackingBottomButtons(
    status: LiveWorkoutStatus,
    onStartClicked: () -> Unit = {},
    onPauseClicked: () -> Unit = {},
    onResumeClicked: () -> Unit = {},
    onFinishClicked: () -> Unit = {},
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        @Composable
        fun StartButton() {
            LiveTrackingButton(
                text = stringResource(R.string.start),
                modifier = Modifier.weight(1f),
                onClick = onStartClicked
            )
        }

        @Composable
        fun PauseButton() {
            LiveTrackingButton(
                text = stringResource(R.string.pause),
                modifier = Modifier.weight(1f),
                onClick = onPauseClicked
            )
        }

        @Composable
        fun ResumeButton() {
            LiveTrackingButton(
                text = stringResource(R.string.resume),
                modifier = Modifier.weight(1f),
                onClick = onResumeClicked
            )
        }

        @Composable
        fun FinishButton(outlined: Boolean = false) {
            LiveTrackingButton(
                text = stringResource(R.string.finish),
                modifier = Modifier.weight(1f),
                onClick = onFinishClicked,
                outlined = outlined
            )
        }

        when (status) {
            LiveWorkoutStatus.NOT_STARTED -> StartButton()
            LiveWorkoutStatus.PAUSED -> {
                ResumeButton()
                HorizontalSpacer(size = 18.dp)
                FinishButton(outlined = true)
            }

            LiveWorkoutStatus.LIVE -> {
                PauseButton()
                HorizontalSpacer(size = 18.dp)
                FinishButton()
            }

            LiveWorkoutStatus.FINISHED -> {}
        }
    }
}

@Composable
fun LiveTrackingButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    outlined: Boolean = false,
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
    telemetry: Float = 14f,
    telemetryUnit: String? = null,
    telemetryLabel: String = "<>",
    integerFormatting: Boolean = false,
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
                    text = NumberUtils.formatNumber(
                        telemetry,
                        integerFormatting = integerFormatting
                    ),
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

enum class LiveWorkoutStatus {
    NOT_STARTED, PAUSED, LIVE, FINISHED;

    fun isTrackable(): Boolean {
        return this == LIVE || this == PAUSED
    }

    fun isPaused(): Boolean {
        return this == PAUSED
    }

    fun isFinished(): Boolean {
        return this == FINISHED
    }

    fun isLive(): Boolean {
        return this == LIVE
    }

}

@Composable
fun LiveTrackingTime(
    modifier: Modifier = Modifier,
    time: String = "00:00",
    workoutStatus: LiveWorkoutStatus,
) {
    val statusTextStyle: TextStyle
    val timeTextStyle: TextStyle
    val contentColor: Color
    val statusText: String? = when (workoutStatus) {
        LiveWorkoutStatus.NOT_STARTED -> stringResource(R.string.status_not_started)
        LiveWorkoutStatus.PAUSED -> stringResource(R.string.status_paused)
        LiveWorkoutStatus.LIVE -> null
        LiveWorkoutStatus.FINISHED -> stringResource(R.string.status_finished)
    }

    when (workoutStatus) {
        LiveWorkoutStatus.LIVE -> {
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

interface LiveTrackingEventsListener {
    fun onCountDownFinished()
    fun onStartClicked()
    fun onPauseClicked()
    fun onResumeClicked()
    fun onFinishClicked()

    companion object {
        fun empty(): LiveTrackingEventsListener {
            return object : LiveTrackingEventsListener {
                override fun onCountDownFinished() {}
                override fun onStartClicked() {}
                override fun onPauseClicked() {}
                override fun onResumeClicked() {}
                override fun onFinishClicked() {}
            }
        }
    }
}

@Composable
fun LiveTrackingScreen(
    uiState: LiveTrackingUiState,
    listener: LiveTrackingEventsListener = LiveTrackingEventsListener.empty(),
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        MapView(
            modifier = Modifier.weight(1f),
            locationCoordinates = uiState.locationCoordinates
        )

        Column(
            Modifier.fillMaxWidth()
        ) {
            Surface {
                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = uiState.locationCoordinates.speedOrNull()?.toBigDecimal()?.setScale(3, RoundingMode.UP)?.toFloat()?.toString()?:"0")
                    LiveTrackingActivityLabel(
                        activityLabel = stringResource(id = uiState.workoutType.nameRes),
                        status = uiState.workoutStatus
                    )
                    LiveTrackingTime(
                        Modifier
                            .align(Alignment.CenterHorizontally),
                        time = uiState.liveTimeDifference.formatForDisplay(),
                        workoutStatus = uiState.workoutStatus
                    )
                    LiveTrackingSpaceEvenRow {
                        LiveTrackingTelemetryItem(
                            telemetry = uiState.distance.getSIValue(),
                            telemetryUnit = uiState.distance.unit.shortSymbol(),
                            telemetryLabel = stringResource(R.string.distance)
                        )
                        LiveTrackingTelemetryItem(
                            telemetry = uiState.stepsCount.toFloat(),
                            telemetryLabel = stringResource(R.string.steps),
                            integerFormatting = true
                        )
                        LiveTrackingTelemetryItem(
                            telemetry = uiState.speed.getSIValue(),
                            telemetryUnit = uiState.speed.unit.shortSymbol(),
                            telemetryLabel = stringResource(R.string.speed)
                        )
                    }
                    VerticalSpacer(size = 32.dp)
                    LiveTrackingBottomButtons(
                        status = uiState.workoutStatus,
                        onStartClicked = { listener.onStartClicked() },
                        onPauseClicked = { listener.onPauseClicked() },
                        onResumeClicked = { listener.onResumeClicked() },
                        onFinishClicked = { listener.onFinishClicked() },
                    )
                }
            }
        }
    }
}

@Composable
fun LiveTrackingDeciderScreen(
    uiState: LiveTrackingUiState,
    listener: LiveTrackingEventsListener = LiveTrackingEventsListener.empty(),
) {
    when (uiState.countDownFinished) {
        false -> LiveTrackingCountDownScreen(onCompleted = { listener.onCountDownFinished() })
        true -> LiveTrackingScreen(uiState, listener = listener)
    }
}

@Composable
fun LiveTrackingCountDownScreen(
    onCompleted: () -> Unit = {},
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

private fun testUiState(): LiveTrackingUiState = LiveTrackingUiState(
    workoutType = WorkoutTypes.TYPE_RUNNING,
    workoutStatus = LiveWorkoutStatus.PAUSED,
    speed = Speed(30.14f),
    distance = Length(15.2f),
    stepsCount = 1200,
    liveTimeDifference = TimeDifference.now(System.currentTimeMillis() - 1000 * 60 * 60),
    pausedTimeDifference = TimeDifference.zero(),
    locationCoordinates = GeoLocation(0.00, 0.00),
    countDownFinished = true
)

//@Preview(name = "Before", group = "LiveTracking")
@Composable
private fun LiveTrackingBeforeScreenPrev() {
    HarmonyTheme {
        Surface {
            LiveTrackingDeciderScreen(uiState = testUiState())
        }
    }
}

@Preview(name = "After", group = "LiveTracking")
@Composable
private fun LiveTrackingAfterScreenPrev() {
    HarmonyTheme {
        Surface {
            LiveTrackingScreen(uiState = testUiState())
        }
    }
}