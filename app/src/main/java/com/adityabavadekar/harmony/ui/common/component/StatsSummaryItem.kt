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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ColorUtils
import com.adityabavadekar.harmony.utils.SleepUtils
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries

private val homeScreenItemPaddingVertical = 16.dp
private val homeScreenItemPaddingHorizontal = 18.dp

@Composable
private fun getStatsItemSurfaceColor(): Color {
    return ColorUtils.getCardColor()
//    return MaterialTheme.colorScheme.surfaceContainerHighest
}

@Composable
fun CircularStatsItem(
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    largeText: String,
    smallText: String,
) {
    Column(
        modifier
//            .border(BorderStroke(2.dp, Color.Gray.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
            .size(80.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickableRipple(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .alpha(0.7f),
            text = largeText,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .alpha(0.7f),
            text = smallText,
        )
    }
}

@Composable
fun StatsChartItem(
    modifier: Modifier = Modifier,
    displayStatNumber: Int,
    displayStatUnit: String? = null,
) {
    val centerChartSize = 150.dp
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clip(CircleShape)
                .clickableRipple()
        ) {
            RingChart(
                Modifier
                    .padding(28.dp),
                size = centerChartSize,
                strokeWidth = 30f
            ) {
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = if (displayStatUnit == null) "$displayStatNumber" else "$displayStatNumber $displayStatUnit",
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        CircularStatsItem(
            modifier = Modifier.align(Alignment.BottomStart),
            largeText = "20",
            smallText = "Cal",
            backgroundColor = getStatsItemSurfaceColor()
        )
        CircularStatsItem(
            modifier = Modifier.align(Alignment.BottomEnd),
            largeText = "1.15",
            smallText = "hrs",
            backgroundColor = getStatsItemSurfaceColor()
        )
    }
}


@Composable
fun WeekCompletionGraphItem(modifier: Modifier = Modifier) {
    val weekNames = listOf("M", "T", "W", "T", "F", "S", "S")
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(weekNames.size) {
            RingChart(
                size = 28.dp,
                strokeWidth = 10f
            ) {
                Text(
                    modifier = Modifier.alpha(0.8f),
                    text = weekNames[it],
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DailyTarget(modifier: Modifier = Modifier) {
    HomeScreenListItem {
        Label(text = "Daily Target")
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            WeekCompletionGraphItem()
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .alpha(0.9f),
                    text = "Completed",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
                HorizontalSpacer()
                Text(
                    modifier = Modifier
                        .alpha(0.9f),
                    text = "0",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                HorizontalSpacer()
                Text(
                    modifier = Modifier
                        .alpha(0.9f),
                    text = "of",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
                HorizontalSpacer()
                Text(
                    modifier = Modifier
                        .alpha(0.9f),
                    text = "7",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                HorizontalSpacer()
                Text(
                    modifier = Modifier
                        .alpha(0.9f),
                    text = "times",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// TODO
@Composable
fun MonthWeightGraphItem(
    modifier: Modifier = Modifier,
    monthCount: Int = 4,
//    timestampToWeightValues: Map<Long, Float>
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) { modelProducer.runTransaction { lineSeries { series(4, 12, 8, 16) } } }
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
        ),
        modelProducer,
    )
}


@Composable
fun WeightTarget(modifier: Modifier = Modifier) {
    HomeScreenListItem {
        Label(text = "Weight")
        VerticalSpacer()
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            //MonthWeightGraphItem()
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .alpha(0.9f),
                    text = "0",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StepsCount(modifier: Modifier = Modifier) {
    HomeScreenListItem {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Label(text = "Steps goal")
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "400",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "/9,000 steps")
                    }
                }
            }
            Box {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.steps),
                    contentDescription = "Water Drop",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}


@Composable
fun QuickNewWorkoutAccess(
    modifier: Modifier = Modifier,
    onClick: (WorkoutTypes) -> Unit = {},
) {
    val items = listOf(
        WorkoutTypes.TYPE_WALKING,
        WorkoutTypes.TYPE_RUNNING,
        WorkoutTypes.TYPE_CYCLING,
        WorkoutTypes.TYPE_OTHER,
    )
    HomeScreenListItem(clickable = false, addPadding = false) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),

            ) {
            Label(
                text = "Quick access"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items.forEach { item ->
                    Surface(
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            Modifier
                                .clickableRipple(onClick = {
                                    onClick(item)
                                })
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(8.dp),
                                painter = painterResource(id = item.drawableRes),
                                contentDescription = stringResource(id = item.nameRes)
                            )
                            Text(
                                text = stringResource(id = item.nameRes),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WaterIntakeCount(modifier: Modifier = Modifier) {
    val hydrationUses = listOf(
        "Keeps skin healthy",
        "Aids digestion",
        "Regulates body temperature",
        "Flushes out toxins",
        "Boosts energy",
        "Supports weight loss",
        "Improves concentration"
    )

    HomeScreenListItem {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Label(text = "Water intake")
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "4",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalSpacer()
                        Text(text = "/15 litres")
                    }
                }
                Box {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.water_drop),
                        contentDescription = "Water Drop",
                        tint = Color.Blue
                    )
                }
            }
            VerticalSpacer()
            Divider()
            Text(
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(top = 8.dp),
                text = "Did you know? Drinking water ${hydrationUses.random()}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SleepTargetCount(modifier: Modifier = Modifier) {

    val sleepHours = 20
    val ageInMonths = 15
    var sleepInfoBannerBackgroundColor = MaterialTheme.colorScheme.primary
    var sleepInfoBannerTextColor = MaterialTheme.colorScheme.surface
    var sleepInfoBannerText = ""
    val requiredSleepRange = SleepUtils.getSleepInfoForAge(ageInMonths).goodRange

    when (SleepUtils.getSleepType(sleepHours, ageInMonths)) {
        SleepUtils.SleepType.RECOMMENDED -> {
            sleepInfoBannerBackgroundColor = MaterialTheme.colorScheme.primary
            sleepInfoBannerTextColor = MaterialTheme.colorScheme.surface
            sleepInfoBannerText = "Great job! Adequate sleep boosts your health."
        }

        SleepUtils.SleepType.INSUFFICIENT -> {
            sleepInfoBannerBackgroundColor =
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 1f)
            sleepInfoBannerTextColor = MaterialTheme.colorScheme.error
            sleepInfoBannerText =
                "You are getting less sleep than recommended.\nConsider aiming for " +
                        "${requiredSleepRange.first}" +
                        "-" +
                        "${requiredSleepRange.last}" +
                        " hours of sleep for better health."
        }

        SleepUtils.SleepType.OVERSLEEPING -> {
            sleepInfoBannerBackgroundColor = ColorUtils.getWarningColor().copy(alpha = 0.3f)
            sleepInfoBannerTextColor = MaterialTheme.colorScheme.onSurface
            sleepInfoBannerText =
                "Excessive sleep can impact productivity and energy.\n" +
                        "Consult a healthcare professional if needed.\n" +
                        "${requiredSleepRange.first}" +
                        "-" +
                        "${requiredSleepRange.last}" +
                        " hours of sleep is ideal for your age."
        }
    }

    HomeScreenListItem(
        addPadding = false
    ) {
        Column(
            Modifier
                .padding(
                    top = homeScreenItemPaddingVertical,
                    start = homeScreenItemPaddingHorizontal,
                    end = homeScreenItemPaddingHorizontal,
                    bottom = 8.dp
                )
        ) {
            Label(text = "Sleep routine")
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Avg sleep duration: ")
                    HorizontalSpacer()
                    Text(
                        text = "$sleepHours hrs",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            VerticalSpacer()
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(sleepInfoBannerBackgroundColor)
                    .padding(
                        PaddingValues(
                            horizontal = homeScreenItemPaddingHorizontal,
                            vertical = homeScreenItemPaddingVertical
                        )
                    )
            ) {
                Text(
                    modifier = Modifier
                        .alpha(0.7f),
                    text = sleepInfoBannerText,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = sleepInfoBannerTextColor
                )
            }
        }
    }
}

@Composable
fun HomeScreenListItem(
    clickable: Boolean = true,
    addPadding: Boolean = true,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(16.dp),
        color = getStatsItemSurfaceColor(),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .then(
                    if (clickable) Modifier.clickableRipple()
                    else Modifier
                )
                .padding(
                    if (addPadding) PaddingValues(
                        horizontal = homeScreenItemPaddingHorizontal,
                        vertical = homeScreenItemPaddingVertical
                    ) else PaddingValues()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            content()
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

private fun LazyListScope.statsItem(
    content: @Composable () -> Unit,
) {
    item {
        content()
    }
}

@Composable
fun StatsSummaryScreen(
    onAddNewClicked: (WorkoutTypes) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        LazyColumn {

            statsItem {
                StatsChartItem(displayStatNumber = 8)
            }
            statsItem {
                StepsCount()
            }
            statsItem {
                QuickNewWorkoutAccess(onClick = onAddNewClicked)
            }
            statsItem {
                DailyTarget()
            }
            statsItem {
                SleepTargetCount()
            }
            statsItem {
                WaterIntakeCount()
            }
            statsItem {
                WeightTarget()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestItem() {
    HarmonyTheme {
        StatsSummaryScreen()
    }
}