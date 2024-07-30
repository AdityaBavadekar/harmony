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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.Speed
import com.adityabavadekar.harmony.ui.common.component.AnimationComponents
import com.adityabavadekar.harmony.ui.common.component.ComposeMapView
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.component.animations.CountDownAnimation
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ConfigurationUtils
import com.adityabavadekar.harmony.utils.LandscapePreview
import com.adityabavadekar.harmony.utils.NumberUtils

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    locationCoordinates: GeoLocation,
    showMapView: Boolean = true
) {
    Column(
        modifier
            .fillMaxWidth()
    ) {
        if (showMapView) {
            ComposeMapView(
                initialize = true,
                point = locationCoordinates,
                disableTouchControls = true
            ) {
                //TODO
                //it.onPause()
                //it.onDetach()
                //it.onResume()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green)
            )
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
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            //color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .then(
                        if (status.isLive()) Modifier.background(AnimationComponents.gradientAnimation())
                        else Modifier
                    )

            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .alpha(0.9f),
                        text = activityLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    HorizontalSpacer(size = 8.dp)
                    /*Box(
                        Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (status.isLive()) {
                            LotieLoader(
                                rawFileRes = R.raw.live_anim,
                                speed = LottieAnimationSpeed.SPEED_2X
                            )
                        } else {
                            Surface(
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                shape = CircleShape
                            ) {
                                Box(
                                    Modifier.size(16.dp),
                                    content = {}
                                )
                            }
                        }
                    }*/
                }
            }
        }
    }
}

@Composable
fun LiveTrackingBottomButtons(
    modifier: Modifier = Modifier,
    status: LiveWorkoutStatus,
    onStartClicked: () -> Unit = {},
    onPauseClicked: () -> Unit = {},
    onResumeClicked: () -> Unit = {},
    onFinishClicked: () -> Unit = {},
    addExtraPadding: Boolean = true,
) {

    Row(
        modifier.fillMaxWidth(),
    ) {
        @Composable
        fun StartButton() {
            LiveTrackingButton(
                text = stringResource(R.string.start),
                modifier = Modifier.weight(1f),
                onClick = onStartClicked,
                addExtraPadding = addExtraPadding
            )
        }

        @Composable
        fun PauseButton() {
            LiveTrackingButton(
                text = stringResource(R.string.pause),
                modifier = Modifier.weight(1f),
                onClick = onPauseClicked,
                addExtraPadding = addExtraPadding
            )
        }

        @Composable
        fun ResumeButton() {
            LiveTrackingButton(
                text = stringResource(R.string.resume),
                modifier = Modifier.weight(1f),
                onClick = onResumeClicked,
                addExtraPadding = addExtraPadding
            )
        }

        @Composable
        fun FinishButton(outlined: Boolean = false) {
            LiveTrackingButton(
                text = stringResource(R.string.finish),
                modifier = Modifier.weight(1f),
                onClick = onFinishClicked,
                outlined = outlined,
                addExtraPadding = addExtraPadding
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
    addExtraPadding: Boolean = true
) {
    var paddingValues = PaddingValues(vertical = 18.dp, horizontal = 32.dp)
    if (!addExtraPadding) paddingValues = PaddingValues(8.dp)
    if (outlined) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            contentPadding = paddingValues,
            onClick = onClick
        ) {
            Text(text = text)
        }
    } else {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
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
    telemetry: String,
    telemetryUnit: String? = null,
    telemetryLabel: String = "<>",
) {
    val isLandscape = ConfigurationUtils.isLandscape()
    val telemetryStyle: TextStyle
    val telemetryUnitStyle: TextStyle
    if (isLandscape) {
        telemetryStyle = MaterialTheme.typography.headlineMedium
        telemetryUnitStyle = MaterialTheme.typography.labelSmall
    } else {
        telemetryStyle = MaterialTheme.typography.headlineLarge
        telemetryUnitStyle = MaterialTheme.typography.labelSmall
    }

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
                    text = telemetry,
                    style = telemetryStyle,
                    fontWeight = FontWeight.Bold,
                )
                if (!isLandscape) VerticalSpacer()
                Text(
                    modifier = Modifier.alpha(0.9f),
                    text = telemetryUnit ?: "",
                    style = telemetryUnitStyle
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
    NOT_STARTED,
    PAUSED,
    LIVE,
    FINISHED;

    /**
     * Returns true if the value is equal to LIVE or PAUSED
     * */
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
    val isLandscape = ConfigurationUtils.isLandscape()

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
            if (isLandscape) {
                timeTextStyle = MaterialTheme.typography.displaySmall
            } else {
                timeTextStyle = MaterialTheme.typography.displayLarge
            }
        }

        else -> {
            contentColor = MaterialTheme.colorScheme.tertiary
            if (isLandscape) {
                statusTextStyle = MaterialTheme.typography.displaySmall
                timeTextStyle = MaterialTheme.typography.titleLarge
            } else {
                statusTextStyle = MaterialTheme.typography.displayLarge
                timeTextStyle = MaterialTheme.typography.displaySmall
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val timeText = @Composable {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = time,
                    style = timeTextStyle,
                    fontWeight = FontWeight.Bold,
                )
                VerticalSpacer()
                Text(text = "Time")
            }
        }
        val statusTextItem = @Composable {
            Text(
                modifier = Modifier.alpha(0.9f),
                text = statusText ?: "",
                style = statusTextStyle,
                fontWeight = FontWeight.Bold,
            )
        }
        Surface(
            contentColor = contentColor,
        ) {
            Column(
                modifier = Modifier
                    .then(
                        if (isLandscape) Modifier
                        else Modifier
                            .height(160.dp)
                            .padding(8.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(visible = statusText != null) {
                    if (isLandscape) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            statusTextItem()
                            timeText()
                        }
                    } else {
                        statusTextItem()
                    }
                }
                if (!isLandscape) timeText()
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
    showMapView: Boolean = true,
) {
    val isLandscape = ConfigurationUtils.isLandscape()

    // TODO: screenWidthDp = 311.dp ? Adjust layout
    @Composable
    fun mapContent(modifier: Modifier) {
        MapView(
            modifier = modifier,
            locationCoordinates = uiState.locationCoordinates,
            showMapView = showMapView
        )
    }

    @Composable
    fun telemetryContent(modifier: Modifier = Modifier) {
        Column(
            modifier.fillMaxWidth()
        ) {
            Surface {
                Column(
                    Modifier
                        .then(
                            if (isLandscape) Modifier.fillMaxHeight()
                            else Modifier.fillMaxWidth()
                        )
                        .padding(
                            horizontal = if (isLandscape) 12.dp else 18.dp,
                            vertical = if (isLandscape) 8.dp else 18.dp
                        ),
                    verticalArrangement = if (!isLandscape) Arrangement.Center else Arrangement.SpaceEvenly,
                ) {
                    Text(
                        text = uiState.heatUnit.toSI(uiState.caloriesBurned)
                            .toString() + " " + uiState.heatUnit.shortSymbol()
                    )
                    LiveTrackingActivityLabel(
                        activityLabel = stringResource(id = uiState.workoutType.nameRes),
                        status = uiState.workoutStatus
                    )
                    LiveTrackingTime(
                        Modifier.align(Alignment.CenterHorizontally),
                        time = uiState.liveTimeDifference.formatForDisplay(),
                        workoutStatus = uiState.workoutStatus
                    )
                    LiveTrackingSpaceEvenRow {
                        LiveTrackingTelemetryItem(
                            telemetry = NumberUtils.formatDouble(
                                uiState.distanceUnit.fromSI(uiState.distance.getSIValue())
                            ),
                            telemetryUnit = uiState.distanceUnit.shortSymbol(),
                            telemetryLabel = stringResource(R.string.distance)
                        )
                        LiveTrackingTelemetryItem(
                            telemetry = uiState.stepsCount.toString(),
                            telemetryLabel = stringResource(R.string.steps)
                        )
                        LiveTrackingTelemetryItem(
                            telemetry = NumberUtils.formatDouble(
                                uiState.speedUnit.fromSI(uiState.speed.getSIValue())
                            ),
                            telemetryUnit = uiState.speedUnit.shortSymbol(),
                            telemetryLabel = stringResource(R.string.speed)
                        )
                    }
                    VerticalSpacer(size = if (isLandscape) 0.dp else 32.dp)
                    LiveTrackingBottomButtons(
                        status = uiState.workoutStatus,
                        onStartClicked = { listener.onStartClicked() },
                        onPauseClicked = { listener.onPauseClicked() },
                        onResumeClicked = { listener.onResumeClicked() },
                        onFinishClicked = { listener.onFinishClicked() },
                        addExtraPadding = if (isLandscape) !uiState.workoutStatus.isPaused() else true
                    )
                }
            }
        }
    }

    if (isLandscape) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            telemetryContent(modifier = Modifier
                .weight(1f)
                .navigationBarsPadding())
            mapContent(modifier = Modifier
                .weight(1f)
                .navigationBarsPadding())
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            mapContent(modifier = Modifier
                .weight(1f)
                .navigationBarsPadding())
            telemetryContent(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
fun LiveTrackingDeciderScreen(
    uiState: LiveTrackingUiState,
    listener: LiveTrackingEventsListener = LiveTrackingEventsListener.empty(),
    showMapView: Boolean = true
) {
    when (uiState.countDownFinished) {
        false -> LiveTrackingCountDownScreen(onCompleted = { listener.onCountDownFinished() })
        true -> LiveTrackingScreen(
            uiState,
            listener = listener,
            showMapView = showMapView
        )
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
                textSize = COUNTDOWN_TEXT_SIZE,
                onCompleted = onCompleted
            )
        }
    }
}

private fun testUiState(): LiveTrackingUiState = LiveTrackingUiState(
    workoutType = WorkoutTypes.TYPE_RUNNING,
    workoutStatus = LiveWorkoutStatus.PAUSED,
    speed = Speed(30.14),
    distance = Length(15.2),
    stepsCount = 1200,
    liveTimeDifference = TimeDifference.now(System.currentTimeMillis() - 10 * 60 * 60),
    pausedTimeDifference = TimeDifference.zero(),
    locationCoordinates = GeoLocation.empty(),
    countDownFinished = true
)

//@Preview(name = "Workout Countdown", group = "LiveTracking")
@Composable
private fun LiveTrackingCountDownPrev() {
    HarmonyTheme {
        LiveTrackingDeciderScreen(
            uiState = testUiState().copy(countDownFinished = false),
            showMapView = false
        )
    }
}

//@LandscapePreview
//@Preview(name = "Workout Live", group = "LiveTracking")
@Composable
private fun LiveTrackingLivePrev() {
    HarmonyTheme {
        LiveTrackingScreen(
            uiState = testUiState().copy(workoutStatus = LiveWorkoutStatus.LIVE),
            showMapView = false
        )
    }
}

@Preview(name = "Workout Paused", group = "LiveTracking")
@LandscapePreview
@Composable
private fun LiveTrackingPausedPrev() {
    HarmonyTheme {
        LiveTrackingScreen(
            uiState = testUiState().copy(workoutStatus = LiveWorkoutStatus.PAUSED),
            showMapView = false
        )
    }
}

private val COUNTDOWN_TEXT_SIZE = 250.sp