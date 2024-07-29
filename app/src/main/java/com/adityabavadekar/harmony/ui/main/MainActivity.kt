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

package com.adityabavadekar.harmony.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adityabavadekar.harmony.ui.livetracking.LiveTrackingActivity
import com.adityabavadekar.harmony.ui.settings.SettingsActivity
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

class MainActivity : ComponentActivity() {

    private val viewModel : MainActivityViewModel by viewModels {
        MainActivityViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HarmonyTheme {
                HarmonyMainApp(
                    navigationToSettings = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    },
                    onAddNewClicked = { type ->
                        //TODO
                        Intent(this, LiveTrackingActivity::class.java).also {
                            it.putExtra(
                                LiveTrackingActivity.EXTRA_WORKOUT_TYPE_ORDINAL,
                                type.ordinal
                            )
                            startActivity(it)
                        }
                    },
                    workoutsData = viewModel.workouts.collectAsStateWithLifecycle(),
                    accountData = viewModel.account.collectAsStateWithLifecycle(),
                )
            }
        }
    }
}