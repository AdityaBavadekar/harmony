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

package com.adityabavadekar.harmony.ui.workoutlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.ui.common.CommonMenuActions
import com.adityabavadekar.harmony.ui.common.component.HarmonySearchInputTopAppBar
import com.adityabavadekar.harmony.ui.common.component.HarmonyTopAppBar
import com.adityabavadekar.harmony.ui.common.component.Label
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.utils.capFirstChar

@Preview
@Composable
fun SelectNewWorkoutScreen(
    modifier: Modifier = Modifier,
    onClick: (type: WorkoutTypes) -> Unit = {},
    onShouldFinish: () -> Unit = {}
) {

    var searchExpanded by rememberSaveable { mutableStateOf(false) }
    var visibleWorkoutTypes by remember { mutableStateOf(WorkoutTypes.entries.toList()) }

    val context = LocalContext.current

    fun restoreList() {
        visibleWorkoutTypes = WorkoutTypes.entries.toList()
    }

    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                AnimatedContent(targetState = searchExpanded, label = "") { isExpanded ->
                    if (!isExpanded) {
                        Surface(color = MaterialTheme.colorScheme.surface) {
                            HarmonyTopAppBar(
                                titleRes = R.string.select_workout_to_continue,
                                navigationIcon = {
                                    Icon(
                                        imageVector = HarmonyIcons.ArrowBack,
                                        contentDescription = null
                                    )
                                },
                                onNavigationIconClicked = {
                                    onShouldFinish()
                                },
                                actionIcons = listOf(
                                    CommonMenuActions.search()
                                ),
                                onActionIconClicked = {
                                    searchExpanded = true
                                },
                            )
                        }
                    } else {
                        HarmonySearchInputTopAppBar(
                            onBackClicked = {
                                searchExpanded = false
                                restoreList()
                            },
                            onSearchTextChanged = { q ->
                                val query = q.lowercase()

                                if (query.trim().isEmpty()) {
                                    restoreList()
                                    return@HarmonySearchInputTopAppBar
                                }

                                visibleWorkoutTypes = WorkoutTypes.entries.filter { workoutType ->
                                    context.getString(workoutType.nameRes).lowercase()
                                        .contains(query) ||
                                            workoutType.category.any { cat ->
                                                cat.name.lowercase().contains(
                                                    query
                                                )
                                            }
                                }
                            }
                        )
                    }
                }
            }

            LazyColumn {
                visibleWorkoutTypes
                    .groupBy { it.category }
                    .forEach { x ->
                        val cats = x.key
                        item {
                            Label(
                                modifier = Modifier.padding(
                                    18.dp
                                ),
                                text = cats.joinToString { it.name.lowercase().capFirstChar() }
                            )
                        }
                        x.value.forEachIndexed { index, item ->
                            item {
                                Column {
                                    ListItem(
                                        modifier = Modifier.clickableRipple(onClick = { onClick(item) }),
                                        headlineContent = {
                                            Text(text = stringResource(id = item.nameRes))
                                        },
                                        leadingContent = {
                                            Icon(
                                                painter = painterResource(id = item.drawableRes),
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }
                                if (index == x.value.lastIndex) {
                                    Divider(Modifier.padding(horizontal = 18.dp))
                                }
                            }
                        }

                    }


            }
        }
    }
}