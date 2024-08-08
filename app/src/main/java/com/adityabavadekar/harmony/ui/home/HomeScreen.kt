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

package com.adityabavadekar.harmony.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.ui.common.component.StatsSummaryScreen
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons

@Preview
@Composable
fun HomeScreen(
    onAddNewClicked: (WorkoutTypes) -> Unit = {},
    homeScreenUiState: HomeScreenUiState = HomeScreenUiState()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddNewClicked(WorkoutTypes.TYPE_UNKNOWN) }) {
                Icon(imageVector = HarmonyIcons.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            StatsSummaryScreen(
                onAddNewClicked = onAddNewClicked,
                homeScreenUiState = homeScreenUiState
            )
        }
    }
}