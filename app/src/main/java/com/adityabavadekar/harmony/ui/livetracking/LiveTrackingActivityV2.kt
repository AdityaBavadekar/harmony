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

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.ui.common.activitybase.PermissionActivityV3
import com.adityabavadekar.harmony.ui.livetracking.service.LiveTrackerForegroundService
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.ui.wdetails.WorkoutDetailActivity
import com.adityabavadekar.harmony.utils.LiveRecordId
import com.adityabavadekar.harmony.utils.UnitPreferences
import com.adityabavadekar.harmony.utils.preferences.preferencesManager


class LiveTrackingActivityV2 : PermissionActivityV3(), LiveTrackingEventsListener {

    private val viewModel: LiveTrackingViewModelV2 by viewModels<LiveTrackingViewModelV2> {
        LiveTrackingViewModelV2.Factory(application)
    }
    private var workoutType: WorkoutTypes? = null

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (LiveRecordId(applicationContext).getId() == null) {
            val workoutTypeOrdinal = intent.getIntExtra(EXTRA_WORKOUT_TYPE_ORDINAL, -1)
            if (workoutTypeOrdinal == -1) {
                throw IllegalStateException("`intent extras should have `LiveTrackingActivity.EXTRA_WORKOUT_TYPE_ORDINAL``")
            }
            workoutType = WorkoutTypes.entries[workoutTypeOrdinal]
            Log.d(TAG, "onCreate: workoutType=${workoutType!!.name}")
            viewModel.setWorkoutType(workoutType!!)
        } else {
            Log.d(TAG, "onCreate: Found existing active workout")
        }

        /* Register with a BroadcastReceiver for getting location updates */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                workoutStatusUpdatesReceiver,
                IntentFilter(LiveTrackerForegroundService.INTENT_WORKOUT_STATUS_ACTION),
                RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(
                workoutStatusUpdatesReceiver,
                IntentFilter(LiveTrackerForegroundService.INTENT_WORKOUT_STATUS_ACTION),
            )
        }

        askLocationPermissions()

        val unitPreferences = UnitPreferences(preferencesManager).getPhysicalUnitPreferences()
        viewModel.setUnits(unitPreferences)

        setContent {
            HarmonyTheme {
                if (viewModel.permissionsGranted.collectAsState().value) {
                    LiveTrackingDeciderScreen(
                        uiState = viewModel.uiState.collectAsState().value,
                        listener = this@LiveTrackingActivityV2,
                    )
                }
            }
        }
    }

    private val workoutStatusUpdatesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (intent.hasExtra(LiveTrackerForegroundService.EXTRA_INTENT_WORKOUT_STATUS)) {
                    val workoutStatus =
                        intent.getStringExtra(LiveTrackerForegroundService.EXTRA_INTENT_WORKOUT_STATUS)
                    if (workoutStatus != null) {
                        Log.d(
                            TAG,
                            "onReceive: workoutStatusUpdates [${intent.action}] [$workoutStatus]"
                        )
                        viewModel.updateStatus(workoutStatus)
                    }
                }
                if (intent.hasExtra(LiveTrackerForegroundService.EXTRA_INTENT_WORKOUT_LOCATION)) {
                    val location =
                        intent.getBundleExtra(LiveTrackerForegroundService.EXTRA_INTENT_WORKOUT_LOCATION)!!
                            .getParcelable<GeoLocation>(LiveTrackerForegroundService.EXTRA_BUNDLE_WORKOUT_LOCATION_KEY)
                    if (location != null) {
                        Log.d(
                            TAG,
                            "onReceive: locationUpdates [${intent.action}]"
                        )
                        viewModel.updateLocation(location)
                    }
                }
                if (intent.hasExtra(LiveTrackerForegroundService.EXTRA_INTENT_WORKOUT_SAVED)) {
                    // Workout Data is saved
                    Log.i(TAG, "onReceive: WORKOUT DATA SAVED SUCCESSFULLY")
                    unregisterReceiver(this)
                    finishAndNavigateToDetail()
                }
            }
        }
    }

    private fun sendActionToService(
        serviceAction: String,
        extraFunction: Intent.() -> Unit = {}
    ) {
        val intent = Intent(this, LiveTrackerForegroundService::class.java).apply {
            action = serviceAction
            apply(extraFunction)
        }
        ContextCompat.startForegroundService(this, intent)
    }

    private fun startTrackerService() {
        Log.d(TAG, "startTrackerService")
        sendActionToService(
            LiveTrackerForegroundService.WORKOUT_ACTION_START
        ) {
            workoutType?.let { type ->
                putExtra(
                    LiveTrackerForegroundService.EXTRA_INTENT_WORKOUT_TYPE,
                    type.name
                )
            }
        }
    }

    private fun resumeTrackerService() {
        Log.d(TAG, "resumeTrackerService")
        sendActionToService(LiveTrackerForegroundService.WORKOUT_ACTION_RESUME)
    }

    private fun pauseTrackerService() {
        Log.d(TAG, "pauseTrackerService")
        sendActionToService(LiveTrackerForegroundService.WORKOUT_ACTION_PAUSE)
    }

    private fun stopTrackerService() {
        Log.d(TAG, "stopTrackerService")
        sendActionToService(LiveTrackerForegroundService.WORKOUT_ACTION_COMPLETE)
    }

    override fun onCountDownFinished() {
        Log.d(TAG, "onCountDownFinished")
        startTrackerService()
        viewModel.startListening()
    }

    override fun onStartClicked() {
        Log.d(TAG, "onStartClicked")
    }

    override fun onPauseClicked() {
        Log.d(TAG, "onPauseClicked")
        viewModel.pause()
        pauseTrackerService()
    }

    override fun onResumeClicked() {
        Log.d(TAG, "onResumeClicked")
        viewModel.resume()
        resumeTrackerService()
    }

    override fun onFinishClicked() {
        Log.d(TAG, "onFinishClicked")
        viewModel.complete()
        stopTrackerService()
    }

    fun finishAndNavigateToDetail() {
        viewModel.recordId?.let { id ->
            Intent(this, WorkoutDetailActivity::class.java).apply {
                putExtra(WorkoutDetailActivity.INTENT_EXTRA_LONG_ID, id)
                putExtra(WorkoutDetailActivity.INTENT_EXTRA_IS_NEW, true)
                startActivity(this)
            }
        }
        finish()
    }

    override fun onLocationPermissionResult(isGranted: Boolean, fromSettings: Boolean) {
        super.onLocationPermissionResult(isGranted, fromSettings)
        Log.d(TAG, "onLocationPermissionResult: isGranted=$isGranted fromSettings=$fromSettings")

        if (isGranted) {
            viewModel.allPermissionsGranted()
        } else {
            if (fromSettings) {
                Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                askLocationPermissions()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(workoutStatusUpdatesReceiver)
        } catch (_: RuntimeException) {
            // java.lang.IllegalArgumentException: Receiver not registered
        }
    }

    companion object {
        private const val TAG = "[LiveTrackingActivity]"
        private const val EXTRA_WORKOUT_TYPE_ORDINAL = "extra_workout_type"

        fun newIntent(context: Context, workoutType: WorkoutTypes) {
            Intent(context, LiveTrackingActivityV2::class.java).apply {
                putExtra(EXTRA_WORKOUT_TYPE_ORDINAL, workoutType.ordinal)
                context.startActivity(this)
            }
        }
    }

}