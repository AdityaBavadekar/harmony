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

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.ui.common.activitybase.PermissionActivityV2
import com.adityabavadekar.harmony.ui.livetracking.service.LiveTrackerService
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.ui.wdetails.WorkoutDetailActivity
import com.adityabavadekar.harmony.utils.ImageAvatar
import com.adityabavadekar.harmony.utils.UnitPreferences
import com.adityabavadekar.harmony.utils.preferences.PreferencesKeys
import com.adityabavadekar.harmony.utils.preferences.preferencesManager
import com.adityabavadekar.harmony.utils.rememberComposeCaptureState


class LiveTrackingActivity : PermissionActivityV2(), LiveTrackingEventsListener,
    SensorEventListener {

    private val viewModel: LiveTrackingViewModel by viewModels<LiveTrackingViewModel> {
        LiveTrackingViewModel.Factory(application)
    }
    private lateinit var sensorManager: SensorManager

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val workoutTypeOrdinal = intent.getIntExtra(EXTRA_WORKOUT_TYPE_ORDINAL, -1)
        if (workoutTypeOrdinal == -1) {
            throw IllegalStateException("`intent extras should have `LiveTrackingActivity.EXTRA_WORKOUT_TYPE_ORDINAL``")
        }
        val workoutType = WorkoutTypes.entries[workoutTypeOrdinal]
        Log.d(TAG, "onCreate: workoutType=${workoutType.name}")
        viewModel.setWorkoutType(workoutType)

        val weight = preferencesManager.getFloat(PreferencesKeys.USER_WEIGHT, 0f)!!
        viewModel.setUserWeight(weight.toDouble())

        /* Start Steps Sensor first so as we get initial value since last reboot and further calculations become accurate. */
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        /* Register with a BroadcastReceiver for getting location updates */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                locationUpdatesReceiver,
                IntentFilter(LiveTrackerService.LOC_UPDATE_INTENT_ACTION),
                RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(
                locationUpdatesReceiver,
                IntentFilter(LiveTrackerService.LOC_UPDATE_INTENT_ACTION)
            )
        }
/*
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
            doOnGranted {
                Log.d(TAG, "onGranted: ACCESS_FINE_LOCATION")
                viewModel.allPermissionsGranted()
            }
            doOnDenied {
                Log.d(TAG, "onDenied: ACCESS_FINE_LOCATION")
                Toast.makeText(
                    this@LiveTrackingActivity,
                    "Location permission is required to show live location",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                false
            }
            doOnShouldShowPermissionUI {
                PermissionsRationaleActivity.startLocationPermissionRationale(applicationContext)
                false
            }
        }*/
        //TODO:Ask Notification Permission

        startLocationService()


        if (stepSensor == null) {
            Log.e(TAG, "STEP SENSOR (`Sensor.TYPE_STEP_COUNTER`) is not present on this device.")
            //throw NotImplementedError("Step Counter Sensor not present.")
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        val unitPreferences = UnitPreferences(preferencesManager)
        val distanceUnit = unitPreferences.distanceUnitPreference()
        val heatUnit = unitPreferences.heatUnitPreference()
        val speedUnit = UnitPreferences.getSpeedUnit(distanceUnit)

        viewModel.setUnits(speedUnit = speedUnit, distanceUnit = distanceUnit, heatUnit = heatUnit)

        setContent {
            val captureState = rememberComposeCaptureState()

            viewModel.setCaptureWorker { name ->
                captureState.capture()
                captureState.bitmap?.let {
                    ImageAvatar.write(
                        applicationContext,
                        name,
                        it
                    )
                }
            }
            HarmonyTheme {
                if (viewModel.permissionsGranted.collectAsState().value) {
                    LiveTrackingDeciderScreen(
                        uiState = viewModel.uiState.collectAsState().value,
                        listener = this@LiveTrackingActivity,
                        captureState = captureState
                    )
                }
            }

        }
    }

    private val locationUpdatesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            LiveTrackerService.decodeLocationUpdatesIntent(intent)?.let {
                onLocationUpdated(it)
            }
        }
    }

    internal fun onLocationUpdated(location: Location) {
        viewModel.onLocationUpdated(location)
    }

    private fun startLocationService() {
        Log.d(TAG, "startLocationService")
        val intent = Intent(this, LiveTrackerService::class.java).apply {
            action = LiveTrackerService.LOC_ACTION_START
        }
        ContextCompat.startForegroundService(this, intent)
    }

    private fun stopLocationService() {
        Log.d(TAG, "stopLocationService")
        val intent = Intent(this, LiveTrackerService::class.java).apply {
            action = LiveTrackerService.LOC_ACTION_STOP
            //startService(this)
        }
        ContextCompat.startForegroundService(this, intent)
    }

    override fun onCountDownFinished() {
        Log.d(TAG, "onCountDownFinished")
        startLocationService()
        viewModel.onCountDownFinished()
    }

    override fun onStartClicked() {
        Log.d(TAG, "onStartClicked")
        //
    }

    override fun onPauseClicked() {
        Log.d(TAG, "onPauseClicked")
        viewModel.pause()
    }

    override fun onResumeClicked() {
        Log.d(TAG, "onResumeClicked")
        viewModel.resume()
    }

    override fun onFinishClicked() {
        Log.d(TAG, "onFinishClicked")
        viewModel.complete()
        stopLocationService()
        unregisterReceiver(locationUpdatesReceiver)
        viewModel.recordId?.let { id ->
            Intent(this, WorkoutDetailActivity::class.java).apply {
                putExtra(WorkoutDetailActivity.INTENT_EXTRA_LONG_ID, id)
                putExtra(WorkoutDetailActivity.INTENT_EXTRA_IS_NEW, true)
                startActivity(this)
            }
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(locationUpdatesReceiver)
        } catch (_: RuntimeException) {
            // java.lang.IllegalArgumentException: Receiver not registered
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            Log.d(TAG, "STEP_SENSOR [onSensorChanged] Event = (null)")
            return
        }
        Log.d(
            TAG,
            "STEP_SENSOR [onSensorChanged] [Accuracy=${event.accuracy}] [${event.values.joinToString()}]"
        )
        if (event.values.isNotEmpty()) {
            viewModel.updateStepsCount(event.values[0].toInt())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "STEP_SENSOR [onAccuracyChanged]: newAccuracy=${accuracy}")
    }


    private fun requestRequiredPermissions() {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    companion object {
        private const val TAG = "[LiveTrackingActivity]"
        const val EXTRA_WORKOUT_TYPE_ORDINAL = "extra_workout_type"
    }

}