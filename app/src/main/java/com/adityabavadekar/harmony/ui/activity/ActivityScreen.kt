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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.WorkoutSummaryRecord
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityScreen(
    workoutsData: List<WorkoutSummaryRecord> = listOf()
) {

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn {
                workoutsData.forEach { record ->
                    item {
                        ActivityItem(record = record)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(
    record: WorkoutSummaryRecord,
) {
    val colorsList = if (isSystemInDarkTheme()) {
        listOf(
            Color(0xFF4A4A4A),
            Color(0xFF3C3C3C),
            Color(0xFF2A2A2A)
        )
    } else {
        listOf(
            Color(0xFFD8EFD3),
            Color(0xFFFEFFD2),
            Color(0xFFE7D4B5),
        )
    }

    Column(
        Modifier
            .fillMaxWidth()
            .clickableRipple()
    ) {
        Row(
            Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Surface(
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
            HorizontalSpacer(size = 18.dp)
            Column(
                Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Column(
                    modifier = Modifier.align(Alignment.End),
                    horizontalAlignment = Alignment.End,
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .alpha(0.9f),
                            text = stringResource(id = record.type.nameRes),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.W400
                        )
                        HorizontalSpacer()
                        Row(Modifier.padding(top = 4.dp)) {
                            Text(
                                modifier = Modifier.alpha(0.7f),
                                text = SimpleDateFormat(
                                    "dd MMM yyyy",
                                    Locale.getDefault()
                                ).format(Date(record.startTimestamp)),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            )
                            Text(
                                modifier = Modifier
                                    .alpha(0.7f)
                                    .padding(horizontal = 2.dp),
                                text = "•",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            )
                            Text(
                                modifier = Modifier.alpha(0.7f),
                                text = record.timeDifference().formatForDisplay(),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    NutItem(
                        text = "${record.totalEnergyBurnedCal ?: 0} cal",
                        color = colorsList[0],
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
                    NutItem(
                        text = "${record.stepsCount}",
                        color = colorsList[1],
                        trailingIcon = R.drawable.steps,
                        trailingIconContentDescription = "Steps count",
                        shape = RoundedCornerShape(0.dp)
                    )
                    NutItem(
                        text = "${record.distanceMeters} m",
                        color = colorsList[2],
                        shape = RoundedCornerShape(
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NutItem(
    text: String,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    @DrawableRes trailingIcon: Int? = null,
    trailingIconContentDescription: String? = null,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor)
    Surface(
        color = color,
        shape = shape
    ) {
        Box(Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = text, style = textStyle)
                HorizontalSpacer(size = 4.dp)
                trailingIcon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = trailingIconContentDescription,
                        modifier = Modifier.size(textStyle.fontSize.value.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActivityScreenPrev() {

    val testData = listOf(
        WorkoutSummaryRecord(
            id = 0,
            type = WorkoutTypes.TYPE_YOGA,
            title = null,
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 1000 * 60,
            distanceMeters = 20000.0,
            stepsCount = 452,
            pauseDurationSec = 1000,
            totalEnergyBurnedCal = 128.0
        )
    )

    HarmonyTheme {
        ActivityScreen(workoutsData = testData)
    }
}