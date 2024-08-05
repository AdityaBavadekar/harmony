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

package com.adityabavadekar.harmony.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.adityabavadekar.harmony.ui.livetracking.LiveTrackingActivityV2
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.ui.workoutlist.SelectNewWorkoutScreen

class PseudoFrontActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenTypeIndex = requireNotNull(intent.getIntExtra(INTENT_EXTRA_SCREEN_ORDINAL, -1))
        val screen = PseudoFrontScreens.entries[screenTypeIndex]

        val onShouldFinish = {
            finish()
        }

        setContent {
            HarmonyTheme {
                when (screen) {
                    PseudoFrontScreens.SELECT_NEW_WORKOUT_SCREEN -> {
                        SelectNewWorkoutScreen(
                            onClick = {
                                LiveTrackingActivityV2.newIntent(this, it)
                                finish()
                            },
                            onShouldFinish = onShouldFinish
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val INTENT_EXTRA_SCREEN_ORDINAL = "intent.extra.screen"

        private enum class PseudoFrontScreens {
            SELECT_NEW_WORKOUT_SCREEN
        }

        fun newSelectWorkoutIntent(context: Context) {
            Intent(context, PseudoFrontActivity::class.java).apply {
                putExtra(
                    INTENT_EXTRA_SCREEN_ORDINAL,
                    PseudoFrontScreens.SELECT_NEW_WORKOUT_SCREEN.ordinal
                )
                context.startActivity(this)
            }
        }

    }

}