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

package com.adityabavadekar.harmony.ui.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.WorkoutSummaryRecord
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ColorUtils
import com.adityabavadekar.harmony.utils.DateFormattingUtils
import com.adityabavadekar.harmony.utils.NumberUtils
import com.adityabavadekar.harmony.utils.ThemePreviews

@Composable
fun ActivityScreen(
    workoutsData: List<WorkoutSummaryRecord> = listOf(),
    onClick: (id: Long, isOngoing: Boolean) -> Unit = { _, _ -> }
) {
    Surface {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (workoutsData.isEmpty()) {
                Column(
                    Modifier
                        .padding(28.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = HarmonyIcons.StrikingStopWatch),
                        contentDescription = null
                    )
                    VerticalSpacer()
                    Text(
                        modifier = Modifier.alpha(0.7f),
                        text = "Your workout history is empty. Add a workout and it will show up here.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium
                    )

                }

            } else {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LazyColumn {
                        workoutsData.forEach { record ->
                            item {
                                ActivityItem(record = record, onClick = onClick)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityItem(
    record: WorkoutSummaryRecord,
    onClick: (id: Long, isOngoing: Boolean) -> Unit = { _, _ -> }
) {
    val distanceCovered = NumberUtils.formatDouble(record.distanceMeters)
    val energyBurned =
        record.totalEnergyBurnedJoules?.let { NumberUtils.formatDouble(it) } ?: 0.0

    val stepCount = NumberUtils.formatInt(record.stepsCount)

    @Composable
    fun workoutIconItem() {
        Surface(
            modifier = Modifier.padding(end = 18.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(Modifier.padding(8.dp)) {
                Icon(
                    painter = painterResource(id = record.type.drawableRes),
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    fun workoutTypeNameItem(modifier: Modifier) {
        Text(
            modifier = modifier
                .alpha(0.9f),
            text = stringResource(id = record.type.nameRes),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W400
        )
    }
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .then(
                    if (!record.completed) {
                        Modifier.background(ColorUtils.getActiveWorkoutListItemColor())
                    } else Modifier
                )
                .clickableRipple(onClick = { onClick(record.id, !record.completed) })
        ) {
            Row(
                Modifier
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                workoutIconItem()
                Column(
                    Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (!record.completed) {
                        Text(
                            text = "Ongoing",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        VerticalSpacer()
                    }
                    Column(
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            workoutTypeNameItem(Modifier.weight(1f))
                            HorizontalSpacer()
                            Row(Modifier.padding(top = 4.dp)) {
                                Text(
                                    modifier = Modifier.alpha(0.7f),
                                    text = DateFormattingUtils.formatWorkoutSummaryDate(record.startTimestamp),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                )
                            }
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.alpha(0.6f),
                                text = "$distanceCovered km",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                            )
                            if (record.completed) {
                                Text(
                                    modifier = Modifier.alpha(0.7f),
                                    text = record.timeDifference()
                                        .formatForDisplayDescriptive(addSeconds = false),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                )
                            }
                        }
                    }
                }
            }
        }
        Divider(Modifier.alpha(0.4f).padding(horizontal = 18.dp))
    }

}

@ThemePreviews
@Composable
private fun ActivityScreenPrev() {

    val testData = listOf(
        WorkoutSummaryRecord(
            id = 0,
            type = WorkoutTypes.TYPE_CYCLING,
            title = null,
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 1000 * 60 * 60 + (1000 * 60 * 23),
            distanceMeters = 20000.0,
            stepsCount = 452,
            pauseDurationMillis = 1000,
            totalEnergyBurnedJoules = 128.0
        )
    )

    HarmonyTheme {
        Surface {
            Column {
                ActivityItem(record = testData[0])
                ActivityItem(record = testData[0])
                ActivityItem(record = testData[0].copy(completed = false))
                ActivityItem(record = testData[0])
            }
        }
    }
}

@Preview
@Composable
private fun EmptySatePreview() {
    HarmonyTheme {
        Surface {
            ActivityScreen()
        }
    }
}