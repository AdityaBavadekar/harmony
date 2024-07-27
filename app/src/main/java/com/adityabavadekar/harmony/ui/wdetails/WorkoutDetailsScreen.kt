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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.common.component.HarmonyTextInput
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons

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

@Composable
fun WorkoutDistanceItem(modifier: Modifier = Modifier) {
    WorkoutListItem(
        labelText = "Distance",
        largeNumberText = "200",
        fillWidth = true,
        horizontalLayout = true,
        largeNumberUnit = "m",
        color = Color.Cyan.copy(alpha = ITEM_COLOR_ALPHA)
    )
}

@Composable
fun WorkoutTotalTimeItem(modifier: Modifier = Modifier) {
    WorkoutListItem(
        modifier = modifier,
        labelText = "Time",
        largeNumberText = "00:20",
        fillWidth = true,
        largeNumberTextStyle = MaterialTheme.typography.headlineLarge,
        largeNumberUnitStyle = MaterialTheme.typography.labelSmall,
        color = Color.Yellow.copy(alpha = ITEM_COLOR_ALPHA)
    )
}

@Composable
fun WorkoutStepsItem(modifier: Modifier = Modifier) {
    WorkoutListItem(
        modifier = modifier,
        labelText = "Steps",
        largeNumberText = "456",
        fillWidth = true,
        largeNumberTextStyle = MaterialTheme.typography.headlineLarge,
        largeNumberUnitStyle = MaterialTheme.typography.labelSmall,
        color = Color.Green.copy(alpha = ITEM_COLOR_ALPHA)
    )
}

@Composable
fun WorkoutTimeItem(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutNoteItem(modifier: Modifier = Modifier) {

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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ListItem(
                    trailing = {
                        Icon(imageVector = HarmonyIcons.ArrowNext, contentDescription = null)
                    },
                    secondaryText = {
                        Text(
                            modifier = Modifier.alpha(0.7f),
                            text = "Trip to my school after years"
                        )
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

@Composable
fun WorkoutTypeInfoItem(modifier: Modifier = Modifier) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = ITEM_COLOR_ALPHA))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cycling",
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Light
        )
        WorkoutDateItem()
    }
}

@Composable
fun WorkoutDateItem(modifier: Modifier = Modifier) {
    Column(
        Modifier
            .padding(18.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.alpha(0.8f),
            text = "On 27 January, 2024, 9:53 am",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun WorkoutDetailsScreen(modifier: Modifier = Modifier) {
    Surface {
        Column(Modifier.fillMaxSize()) {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Green),
                    content = {}
                )
            }

            /* Workout Type */
            WorkoutTypeInfoItem()

            /* Workout Distance */
            WorkoutDistanceItem()

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                WorkoutTotalTimeItem(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                WorkoutStepsItem(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            ListItem (
                modifier = Modifier.background(Color.Gray.copy(alpha = ITEM_COLOR_ALPHA)),
                trailing = {
                    Text(text = "30 kcal", style = MaterialTheme.typography.labelMedium)
                }
            ){
                Text(text = "Energy", style = MaterialTheme.typography.titleMedium)
            }
            ListItem (
                modifier = Modifier.background(Color.Gray.copy(alpha = ITEM_COLOR_ALPHA)),
                trailing = {
                    Text(text = "30 m/s", style = MaterialTheme.typography.labelMedium)
                }
            ){
                Text(text = "Avg. Speed", style = MaterialTheme.typography.titleMedium)
            }
            ListItem (
                modifier = Modifier.background(Color.Gray.copy(alpha = ITEM_COLOR_ALPHA)),
                trailing = {
                    Text(text = "30 kcal", style = MaterialTheme.typography.labelMedium)
                }
            ){
                Text(text = "Laps", style = MaterialTheme.typography.titleMedium)
            }
            ListItem (
                modifier = Modifier.background(Color.Gray.copy(alpha = ITEM_COLOR_ALPHA)),
                trailing = {
                    Text(text = "45 m/s", style = MaterialTheme.typography.labelMedium)
                }
            ){
                Text(text = "Max Speed", style = MaterialTheme.typography.titleMedium)
            }
            //WorkoutNoteItem()
        }
    }
}

private const val ITEM_COLOR_ALPHA = 0.3f