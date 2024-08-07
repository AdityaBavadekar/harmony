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

package com.adityabavadekar.harmony.ui.wdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.data.model.WorkoutLap
import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.component.ComposeMapView
import com.adityabavadekar.harmony.ui.common.component.DefaultColumnChart
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.NumberUtils
import com.adityabavadekar.harmony.utils.ThemePreviews
import com.adityabavadekar.harmony.utils.UnitPreferences
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WorkoutListItem(
    labelText: String,
    largeNumberText: String,
    largeNumberUnit: String? = null,
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    horizontalLayout: Boolean = false,
    labelTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    largeNumberTextStyle: TextStyle = MaterialTheme.typography.displayMedium,
    largeNumberUnitStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    Column(modifier) {
        Surface(
            shape = RoundedCornerShape(0.dp),
            color = color
        ) {
            Column(
                Modifier
                    .then(
                        if (fillWidth) Modifier.fillMaxWidth()
                        else Modifier
                    )
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalSpacer()
                Text(
                    modifier = Modifier.alpha(0.9f),
                    text = labelText,
                    style = labelTextStyle
                )

                if (horizontalLayout) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = largeNumberText,
                            style = largeNumberTextStyle,
                            fontWeight = FontWeight.Bold,
                        )
                        HorizontalSpacer()
                        largeNumberUnit?.let {
                            VerticalSpacer()
                            Text(
                                text = it,
                                style = largeNumberUnitStyle,
                                lineHeight = MaterialTheme.typography.displayMedium.lineHeight,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                } else {
                    Column(
                        Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = largeNumberText,
                            style = largeNumberTextStyle,
                            fontWeight = FontWeight.Bold,
                        )
                        largeNumberUnit?.let {
                            VerticalSpacer()
                            Text(
                                text = it,
                                style = largeNumberUnitStyle,
                                lineHeight = MaterialTheme.typography.displayMedium.lineHeight,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
                VerticalSpacer()
            }
        }
    }
}

fun LazyListScope.workoutDistanceItem(
    distance: String,
    distanceUnit: String,
) {
    item {
        WorkoutListItem(
            labelText = "Distance",
            largeNumberText = distance,
            fillWidth = true,
            horizontalLayout = true,
            largeNumberUnit = distanceUnit,
            color = Color.Cyan.copy(alpha = ITEM_COLOR_ALPHA)
        )
    }
}

@Composable
fun WorkoutTotalTimeItem(
    modifier: Modifier = Modifier,
    totalTimeDifference: TimeDifference,
    pausedTimeDifference: TimeDifference,
) {
    WorkoutListItem(
        modifier = modifier,
        labelText = "Time",
        largeNumberText = totalTimeDifference.formatForDisplay(),
        fillWidth = true,
        largeNumberTextStyle = MaterialTheme.typography.headlineLarge,
        largeNumberUnitStyle = MaterialTheme.typography.labelSmall,
        color = Color.Yellow.copy(alpha = ITEM_COLOR_ALPHA)
    )
}

@Composable
fun WorkoutStepsItem(modifier: Modifier = Modifier, stepsCount: Int) {
    WorkoutListItem(
        modifier = modifier,
        labelText = "Steps",
        largeNumberText = NumberUtils.formatInt(stepsCount),
        fillWidth = true,
        largeNumberTextStyle = MaterialTheme.typography.headlineLarge,
        largeNumberUnitStyle = MaterialTheme.typography.labelSmall,
        color = Color.Green.copy(alpha = ITEM_COLOR_ALPHA)
    )
}

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.workoutNoteItem(
    notes: String?,
    modifier: Modifier = Modifier
) {
    item {
        Column(
            modifier.padding(18.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray.copy(alpha = ITEM_COLOR_ALPHA)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ListItem(
                        trailing = {
                            Icon(imageVector = HarmonyIcons.ArrowNext, contentDescription = null)
                        },
                        secondaryText = {
                            if (notes != null) {
                                Text(
                                    modifier = Modifier.alpha(0.7f),
                                    text = notes
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "Add notes",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.workoutChartItem(
    title: String,
    dataX: List<Double>,
    modifier: Modifier = Modifier
) {
    val dataXAxis = dataX.map { it.toFloat() }
    val dataYAxis = List(dataX.size) { index -> index + 1 }
    val model = CartesianChartModel(
        ColumnCartesianLayerModel.build { series(dataXAxis, dataYAxis) }
    )
    item {
        Column(
            modifier.padding(18.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray.copy(alpha = ITEM_COLOR_ALPHA)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    VerticalSpacer()
                    DefaultColumnChart(model = model)
                }
            }
        }
    }
}

fun LazyListScope.workoutTypeInfoItem(
    workoutType: WorkoutTypes,
    workoutStartDate: Long,
) {
    item {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = ITEM_COLOR_ALPHA))
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = workoutType.nameRes),
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Light
            )
            WorkoutDateItem(timestamp = workoutStartDate)
        }
    }
}

@Composable
fun WorkoutDateItem(timestamp: Long) {
    Column(
        Modifier
            .padding(18.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val displayDate =
            SimpleDateFormat(
                "dd MMMM yyyy, h:mm a",
                Locale.getDefault()
            ).format(Date(timestamp))
        val displayText = "On $displayDate"
        Text(
            modifier = Modifier.alpha(0.8f),
            text = displayText,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun HorizontalDarkDivider() {
    Divider(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onSurface)
}

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.workoutLapsItem(
    laps: List<WorkoutLap>
) {
    if (laps.isEmpty()) return

    item {
        HorizontalDarkDivider()
        Column(Modifier.padding(8.dp)) {
            Column(
                Modifier.padding(
                    top = 18.dp,
                    end = 18.dp,
                    start = 18.dp,
                )
            ) {
                Text(text = "Laps", style = MaterialTheme.typography.titleMedium)
            }
            VerticalSpacer()
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    laps.forEachIndexed { index, lap ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val shape: Shape
                            if (index == 0) shape = RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 8.dp
                            ) else if (index == laps.lastIndex) shape = RoundedCornerShape(
                                bottomStart = 8.dp,
                                bottomEnd = 8.dp
                            ) else {
                                shape = RectangleShape
                            }
                            Surface(
                                shape = shape
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .alpha(0.6f),
                                    text = (index + 1).toString(),
                                )
                            }
                            HorizontalSpacer(size = 18.dp)
                            Text(text = lap.timeDifference().formatForDisplay())
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.workoutSimpleListItem(
    text: String,
    value: String,
    unit: String,
) {
    item {
        ListItem(
            modifier = Modifier.background(Color.Magenta.copy(alpha = ITEM_COLOR_ALPHA)),
            trailing = {
                Row {
                    Text(text = value, style = MaterialTheme.typography.labelMedium)
                    HorizontalSpacer()
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        ) {
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun WorkoutDetailsScreen(
    workoutRecord: WorkoutRecord,
    physicalUnitPreferences: UnitPreferences.PhysicalUnitPreferences,
    showMap: Boolean = false
) {
    val distance =
        workoutRecord.distance().stringRepresentation(physicalUnitPreferences.distanceUnit)
    val caloriesBurned =
        workoutRecord.energyBurned().stringRepresentation(physicalUnitPreferences.heatUnit)
    val avgSpeed =
        workoutRecord.avgSpeed().stringRepresentation(physicalUnitPreferences.speedUnit())
    val maxSpeed =
        workoutRecord.maxSpeed().stringRepresentation(physicalUnitPreferences.speedUnit())
    val minSpeed =
        workoutRecord.minSpeed().stringRepresentation(physicalUnitPreferences.speedUnit())

    HarmonyTheme {
        LazyColumn {
            item {
                if (showMap) {
                    ComposeMapView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MAP_VIEW_HEIGHT),
                        disableTouchControls = true
                    )
                } else {
                    Column {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(MAP_VIEW_HEIGHT)
                                .background(Color.Blue),
                            content = {}
                        )
                    }
                }
            }

            /* Workout Type */
            workoutTypeInfoItem(
                workoutType = workoutRecord.type,
                workoutStartDate = workoutRecord.startTimestamp
            )

            /* Workout Distance */
            workoutDistanceItem(
                distance = distance,
                distanceUnit = physicalUnitPreferences.distanceUnit.shortSymbol()
            )

            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    WorkoutTotalTimeItem(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        totalTimeDifference = workoutRecord.totalTimeDifference(),
                        pausedTimeDifference = workoutRecord.pausedTimeDifference()
                    )

                    WorkoutStepsItem(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        stepsCount = workoutRecord.stepsCount
                    )
                }
            }

            workoutSimpleListItem(
                text = "Energy",
                value = caloriesBurned,
                unit = physicalUnitPreferences.heatUnit.shortSymbol()
            )
            workoutSimpleListItem(
                text = "Avg. speed",
                value = avgSpeed,
                unit = physicalUnitPreferences.speedUnit().shortSymbol()
            )
            workoutSimpleListItem(
                text = "Max. speed",
                value = maxSpeed,
                unit = physicalUnitPreferences.speedUnit().shortSymbol()
            )
            workoutSimpleListItem(
                text = "Min. speed",
                value = minSpeed,
                unit = physicalUnitPreferences.speedUnit().shortSymbol()
            )
            workoutLapsItem(laps = workoutRecord.laps)
            workoutNoteItem(workoutRecord.notes)

            workoutChartItem("Speed", workoutRecord.speedsMetersSec)
        }
    }
}

private const val ITEM_COLOR_ALPHA = 1f

private fun testWorkoutRecord(): WorkoutRecord {
    val x = 142634929L
    val laps = listOf(
        WorkoutLap(x - 5000, x + 8000),
        WorkoutLap(x - 300, x + 8000),
        WorkoutLap(x - 9000, x + 9000),
    )
    return WorkoutRecord(
        type = WorkoutTypes.TYPE_CYCLING,
        startTimestamp = System.currentTimeMillis() - (1000 * 60 * 60 + 40000),
        endTimestamp = System.currentTimeMillis(),
        distanceMeters = LengthUnits.KILOMETERS.toSI(19.5),
        stepsCount = 432,
        laps = laps,
        pauseDurationMillis = laps.sumOf { it.diff() },
        speedsMetersSec = listOf(
            0.0,
            16.0,
            40.0,
            35.0,
            12.0,
            18.0,
            12.0,
            16.0,
            20.0,
            0.0,
            19.0,
            16.0,
            16.0,
            35.0,
            16.0,
        ),
        totalEnergyBurnedJoules = 300.0,
        avgSpeedMetersSec = 16.0,
        maxSpeedMetersSec = 40.0,
        minSpeedMetersSec = 0.0,
        completed = true
    )
}

private val MAP_VIEW_HEIGHT = 300.dp

@ThemePreviews
@Composable
private fun WorkoutDetailScreenPrev() {
    val workoutRecord = testWorkoutRecord()
    HarmonyTheme {
        /*WorkoutDetailsScreen(
            workoutRecord = workoutRecord,
            physicalUnitPreferences = UnitPreferences.PhysicalUnitPreferences.defaults(),
            showMap = false
        )*/
        LazyColumn {
            workoutChartItem("Speed", workoutRecord.speedsMetersSec)
        }
    }
}