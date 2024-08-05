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

package com.adityabavadekar.harmony.ui.livetracking.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.adityabavadekar.harmony.HarmonyApplication
import com.adityabavadekar.harmony.data.WorkoutTypes
import com.adityabavadekar.harmony.data.model.TimeDifference
import com.adityabavadekar.harmony.data.model.WorkoutLap
import com.adityabavadekar.harmony.data.model.WorkoutLocation
import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.database.repo.AccountRepository
import com.adityabavadekar.harmony.database.repo.WorkoutsRepository
import com.adityabavadekar.harmony.ui.common.Speed
import com.adityabavadekar.harmony.ui.common.TimeUnits
import com.adityabavadekar.harmony.ui.livetracking.GeoLocation
import com.adityabavadekar.harmony.ui.livetracking.LiveWorkoutStatus
import com.adityabavadekar.harmony.utils.CaloriesCounter
import com.adityabavadekar.harmony.utils.ColorUtils
import com.adityabavadekar.harmony.utils.LiveRecordId
import com.adityabavadekar.harmony.utils.PermissionUtils
import com.adityabavadekar.harmony.utils.StepsCounter
import com.adityabavadekar.harmony.utils.WorkoutPauseDetector
import com.adityabavadekar.harmony.utils.WorkoutRouteManager
import com.adityabavadekar.harmony.utils.tts.TextToSpeechEngine
import com.adityabavadekar.harmony.utils.withIOContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LiveTrackerForegroundService : Service(), LocationUpdateListener, SensorEventListener {

    private lateinit var locationClient: LocationClient

    private var started: Boolean = false
    private var startTimestamp: Long = 0L
    private var pauses = mutableListOf<WorkoutLap>()
    private var lastPauseTimestamp: Long? = null
    private var forcePausedByUser: Boolean = false
    private val workoutPauseDetector: WorkoutPauseDetector = WorkoutPauseDetector()
    private val workoutRouteManager: WorkoutRouteManager = WorkoutRouteManager()
    private val stepsCounter: StepsCounter = StepsCounter()
    private val caloriesCounter: CaloriesCounter = CaloriesCounter()
    private val speeds: MutableList<Double> = mutableListOf()
    private var userWeight: Double = 0.0
    private var serviceState = MutableStateFlow(LiveTrackingServiceState.nullState())
    private var recordId: Long? = null
    private val job = SupervisorJob()
    private lateinit var sensorManager: SensorManager
    private lateinit var liveRecordId: LiveRecordId
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var textToSpeechEngine: TextToSpeechEngine
    private lateinit var repository: WorkoutsRepository
    private lateinit var accountRepository: AccountRepository

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        locationClient = LiveLocationClient(applicationContext, this)
        init()
        start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: action=${intent?.action}")
        when (intent?.action) {
            WORKOUT_ACTION_START -> start(intent)
            WORKOUT_ACTION_PAUSE -> pause()
            WORKOUT_ACTION_RESUME -> resume()
            WORKOUT_ACTION_COMPLETE -> complete()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient.destroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    private fun startSelf() {
        locationClient.startLocationRetrieval(LOCATION_UPDATES_INTERVAL)

        if (PermissionUtils.areNotificationsAllowed(this)) {
            val notification = buildNotification(applicationContext, serviceState.value.workoutType)
            startForeground(LOCATION_NOTIFICATION_ID, notification.build())
        }
        liveRecordId = LiveRecordId(applicationContext)
        registerStepsCounterReceiver()
        startedTrackerLoop()
    }

    override fun onLocationUpdate(location: Location) {
        Log.d(TAG, "onLocationUpdate: ${GeoLocation.from(location)}")
        onLocationDataUpdated(location)
    }

    @SuppressLint("MissingPermission", "NewApi")
    private fun updateNotification(timeDifference: TimeDifference) {
        if (PermissionUtils.areNotificationsAllowed(this)) {
            val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
            val updatedNotification = buildNotification(
                context = applicationContext,
                workoutType = serviceState.value.workoutType,
                updatedText = timeDifference.formatForDisplay()
            )
            notificationManagerCompat.notify(
                LOCATION_NOTIFICATION_ID,
                updatedNotification.build()
            )
        } else {
            Log.w(
                TAG,
                "onLocationUpdate: Notification Permission not granted!",
                PermissionUtils.PermissionsNotGrantedException(PermissionUtils.notificationPermissions())
            )
        }
    }

    private fun onLocationDataUpdated(location: Location) {
        val geoLocation = GeoLocation.from(location)

        if (geoLocation.isNullLocation()) return
        sendLocationToSource(geoLocation)

        updateServiceState { prevValue ->
            val previousWorkoutStatus = prevValue.workoutStatus
            if (!previousWorkoutStatus.isTrackable()) return@updateServiceState prevValue

            val isPauseDetected = workoutPauseDetector.isPaused(
                geoLocation.speedOrNull(),
                forcePausedByUser
            )
            val newWorkoutStatus: LiveWorkoutStatus = if (isPauseDetected) {
                Log.d(TAG, "onLocationDataUpdated: AUTO PAUSED")
                // Pause is detected, update the Workout Status to Paused
                LiveWorkoutStatus.PAUSED
            } else {
                // Auto Pause is NOT detected
                // If Workout is not paused by the user but the previous workout status is equal to Paused
                // then change the status to Live as Pause is "NOT" detected.
                if (previousWorkoutStatus.isPaused() && !forcePausedByUser) {
                    Log.d(TAG, "onLocationDataUpdated: Activity Detected!! Resuming workout")
                    // Activity was detected even though workout is auto paused.
                    // Thus, change workout status to Live
                    LiveWorkoutStatus.LIVE
                } else previousWorkoutStatus
            }

            if (newWorkoutStatus.ordinal != previousWorkoutStatus.ordinal) {
                //TODO : textToSpeechEngine.speak(getString(newWorkoutStatus.ttsText))
                // Issue: This condition always evaluates to true, just after since AutoPause Detection.
                Log.w(TAG, "onLocationDataUpdated: SPEECH SPOKEN")
            }

            if (newWorkoutStatus.isLive() && geoLocation.hasSpeed()) {
                addNewSpeed(geoLocation.requireSpeed)
            }

            if (!prevValue.locationCoordinates.isNullLocation()) {
                val locationStartTimestamp = prevValue.locationCoordinates.timestamp
                val locationEndTimestamp = System.currentTimeMillis()

                val newlyTravelledDistance = workoutRouteManager.addLocation(
                    WorkoutLocation.fromGeoLocation(
                        startTimestamp = locationStartTimestamp,
                        endTimestamp = locationEndTimestamp,
                        geoLocation = prevValue.locationCoordinates,
                    ),
                    calculateDistance = newWorkoutStatus.isLive()
                )
                if (newWorkoutStatus.isLive()) {
                    caloriesCounter.increment(
                        newDistanceMeters = newlyTravelledDistance,
                        durationSec = TimeDifference.from(
                            startMillis = locationStartTimestamp,
                            endMillis = locationEndTimestamp
                        ).getValue(TimeUnits.SECONDS)
                    )
                }
            }
            sendStatusToSource(newWorkoutStatus)
            prevValue.copy(
                locationCoordinates = geoLocation,
                workoutStatus = newWorkoutStatus
            )
        }
    }

    private fun registerStepsCounterReceiver() {
        /* Start Steps Sensor first so as we get initial value since last reboot and further calculations become accurate. */
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Log.e(TAG, "STEP SENSOR (`Sensor.TYPE_STEP_COUNTER`) is not present on this device.")
            //throw NotImplementedError("Step Counter Sensor not present.")
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
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
            val stepsSinceReboot = event.values[0].toInt()
            stepsCounter.record(stepsSinceReboot)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun startedTrackerLoop() {
        startTimestamp = System.currentTimeMillis()
        updateWorkoutStatus(LiveWorkoutStatus.LIVE)

        var databaseUpdateCounter = 0
        var logCounter = 0
        coroutineScope.launch {
            if (liveRecordId.getId() == null) {
                Log.d(TAG, "startedTrackerLoop: LiveRecord id not present. New id will be created.")
                withIOContext {
                    val id =
                        repository.insertWorkoutRecord(WorkoutRecord.startupRecord(type = serviceState.value.workoutType))
                    recordId = id
                    liveRecordId.set(id)
                    Log.i(TAG, "onCountDownFinished: [[[RECORD ID =$id]]]")
                }
            } else {
                Log.d(TAG, "startedTrackerLoop: Retrieving existing LiveRecord id")
                recordId = liveRecordId.getId()
            }

            Log.d(TAG, "onCountDownFinished::while:TRUE [INITIALLY] ${serviceState.value}")

            while (serviceState.value.workoutStatus.isTrackable()) {

                val distanceTravelled = workoutRouteManager.getDistanceTraversed()
                val totalEnergyBurnedCal = caloriesCounter.getCount()

                /*
                * Update data only if workout is live and Skip if workout is Paused.
                * Since workout can be resumed again, the while loop should keep running until workout is completed.
                * */
                if (lastPauseTimestamp == null) {
                    lastPauseTimestamp = System.currentTimeMillis()
                }

                logCounter++
                databaseUpdateCounter++

                if (databaseUpdateCounter >= 1) {
                    //Update database after every 5 sec
                    /**
                     * DATA:
                     * - pauses
                     * - route
                     * - speeds
                     * - distance
                     * - steps
                     * */
                    withIOContext {
                        val record = repository.getIncompleteWorkoutRecord()

                        if (record != null) {
                            repository.updateWorkoutRecord(
                                record.update(
                                    laps = pauses,
                                    workoutRoute = workoutRouteManager.route(),
                                    speeds = speeds,
                                    distanceMeters = distanceTravelled,
                                    stepsCount = stepsCounter.stepsCount(),
                                    totalEnergyBurnedCal = totalEnergyBurnedCal
                                )
                            )
                            speeds.clear()
                            updateNotification(
                                TimeDifference.now(
                                    startTimestamp,
                                    ignorablePauses = record.laps
                                )
                            )
                        }
                        databaseUpdateCounter = 0
                    }
                }

                if (logCounter >= 8) {
                    Log.i(TAG, "startedTrackerLoop: TRACKING")
                    logCounter = 0
                }

                delay(UPDATE_INTERVAL_MILLIS)
            }
        }
    }

    private fun updatePauses() {
        if (lastPauseTimestamp != null) {
            pauses.add(
                WorkoutLap(
                    lastPauseTimestamp!!,
                    System.currentTimeMillis()
                )
            )
            lastPauseTimestamp = null
        }
    }

    private fun addNewSpeed(speed: Speed) {
        speeds.add(speed.getSIValue())
    }

    private fun start(intent: Intent? = null) {
        intent?.let { i ->
            val workoutTypeString = i.getStringExtra(EXTRA_INTENT_WORKOUT_TYPE)
            if (workoutTypeString != null) {
                val workoutType = WorkoutTypes.valueOf(workoutTypeString)
                updateServiceState { it.copy(workoutType = workoutType) }
                Log.i(TAG, "start: INITIALISED WITH WORKOUT TYPE = ${workoutType.name}")
            }
        }
        if (started) {
            Log.w(TAG, "start: Ignoring second call to start")
            return
        }
        started = true
        Log.d(TAG, "start")
        Log.d(TAG, "start: Starting")
        startSelf()
    }

    private fun pause() {
        Log.d(TAG, "pause")
        forcePausedByUser = true
        updateWorkoutStatus(LiveWorkoutStatus.PAUSED)
    }

    private fun resume() {
        Log.d(TAG, "resume")
        forcePausedByUser = false
        workoutPauseDetector.clear()
        updateWorkoutStatus(LiveWorkoutStatus.LIVE)
    }

    private fun complete() {
        Log.d(TAG, "complete")
        forcePausedByUser = false
        updateWorkoutStatus(LiveWorkoutStatus.FINISHED)
        updatePauses()
        coroutineScope.launch {
            withIOContext {
                val record = repository.getIncompleteWorkoutRecord()
                if (record != null) {
                    Log.d(TAG, "setting: Record status to status completed")
                    val updatedRecord = getFinalWorkoutRecord(record)
                    repository.updateWorkoutRecord(updatedRecord)
                    liveRecordId.clear()
                    Log.d(TAG, "finished: Record status to status completed")
                    sendCompletionStatusToSource()
                } else {
                    // TODO: sendFailureStatusToSource()
                    Log.e(
                        TAG,
                        "Error setting record to completed!!: record was null!! (id=${recordId})",
                        NullPointerException()
                    )
                }
                stop()
            }
        }
    }


    private fun stop() {
        textToSpeechEngine.destroy()
        Log.d(TAG, "stop: Stopping")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun getFinalWorkoutRecord(initialRecord: WorkoutRecord): WorkoutRecord {
        return initialRecord.updateAfterTrackingFinished(
            stepsCount = stepsCounter.stepsCount(),
            distanceMeters = workoutRouteManager.getDistanceTraversed(),
            totalEnergyBurnedCal = caloriesCounter.getCount(),
            workoutRoute = workoutRouteManager.route(),
            laps = pauses,
            speeds = speeds,
            temperatureCelsius = null
        )
    }

    private fun updateServiceState(newServiceState: (prevValue: LiveTrackingServiceState) -> LiveTrackingServiceState?) {
        newServiceState(serviceState.value)?.let { serviceState.value = it }
    }

    private fun updateWorkoutStatus(newWorkoutStatus: LiveWorkoutStatus, speak: Boolean = true) {
        Log.i(
            TAG,
            "updateWorkoutStatus: new=${newWorkoutStatus.name} old=${serviceState.value.workoutStatus.name}"
        )
        if (newWorkoutStatus != serviceState.value.workoutStatus && speak) {
            // Speak only if status has indeed changed.
            textToSpeechEngine.speak(getString(newWorkoutStatus.ttsText))
        }
        updateServiceState { it.copy(workoutStatus = newWorkoutStatus) }
        sendStatusToSource(newWorkoutStatus)
    }

    private fun sendStatusToSource(status: LiveWorkoutStatus) {
        val updatesIntent = Intent(INTENT_WORKOUT_STATUS_ACTION).apply {
            putExtra(EXTRA_INTENT_WORKOUT_STATUS, status.name)
        }
        sendBroadcast(updatesIntent)
    }

    private fun sendCompletionStatusToSource() {
        val updatesIntent = Intent(INTENT_WORKOUT_STATUS_ACTION).apply {
            putExtra(EXTRA_INTENT_WORKOUT_SAVED, true)
        }
        sendBroadcast(updatesIntent)
    }

    private fun sendLocationToSource(locationCoordinates: GeoLocation) {
        val locationUpdatesIntent = Intent(INTENT_WORKOUT_STATUS_ACTION).apply {
            val b = Bundle()
            b.putParcelable(EXTRA_BUNDLE_WORKOUT_LOCATION_KEY, locationCoordinates)
            putExtra(EXTRA_INTENT_WORKOUT_LOCATION, b)
        }
        sendBroadcast(locationUpdatesIntent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.w(TAG, "onUnbind: All clients have disconnected")
        return super.onUnbind(intent)
    }

    private fun init() {
        val app = applicationContext as HarmonyApplication
        repository = app.getWorkoutRepository()
        accountRepository = app.getAccountRepository()
        textToSpeechEngine = TextToSpeechEngine(app)

        coroutineScope.launch {
            withIOContext {
                val account = accountRepository.getAccount()
                account.userFitnessRecord?.weight?.let { userWeight = it.getSIValue() }
            }
        }
    }

    data class LiveTrackingServiceState(
        val workoutType: WorkoutTypes,
        val workoutStatus: LiveWorkoutStatus,
        val locationCoordinates: GeoLocation,
    ) {
        companion object {
            fun nullState(): LiveTrackingServiceState {
                return LiveTrackingServiceState(
                    workoutType = WorkoutTypes.TYPE_UNKNOWN,
                    workoutStatus = LiveWorkoutStatus.NOT_STARTED,
                    locationCoordinates = GeoLocation.empty(),
                )
            }
        }
    }

    companion object {
        private const val TAG = "[LiveTrackerService]"
        private const val UPDATE_INTERVAL_MILLIS = 500L // 0.5sec
        private const val LOCATION_NOTIFICATION_ID = 101
        private const val LOCATION_UPDATES_INTERVAL = 300L //ms
        private const val LOCATION_NOTIFICATION_CHANNEL_ID = "location_channel"

        const val WORKOUT_ACTION_START = "workout_action.start"
        const val WORKOUT_ACTION_PAUSE = "workout_action.pause"
        const val WORKOUT_ACTION_RESUME = "workout_action.resume"
        const val WORKOUT_ACTION_COMPLETE = "workout_action.complete"
        const val EXTRA_INTENT_WORKOUT_SAVED = "workout.saved_success"
        const val EXTRA_INTENT_WORKOUT_STATUS = "workout.status"
        const val EXTRA_INTENT_WORKOUT_TYPE = "workout.type"
        const val EXTRA_INTENT_WORKOUT_LOCATION = "workout.location"
        const val EXTRA_BUNDLE_WORKOUT_LOCATION_KEY = "bundle.workout.location"
        const val INTENT_WORKOUT_STATUS_ACTION = "intent.action_workout.status"

        private fun buildNotification(
            context: Context,
            workoutType: WorkoutTypes,
            updatedText: String? = null
        ): NotificationCompat.Builder {
            val tapToSeeText = "Tap to see workout"
            val contentText = if (updatedText == null) tapToSeeText
            else updatedText + "\n" + tapToSeeText

            val notification = NotificationCompat.Builder(context, LOCATION_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Tracking '" + context.getString(workoutType.nameRes) + "'")
                .setContentText(contentText)
                .setSmallIcon(workoutType.drawableRes) //.setSmallIcon(R.drawable.location_pin)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.Cyan.toArgb())
                .setOngoing(true)
                .setAutoCancel(false)
                .setColorized(true)
                .setColor(ColorUtils.getActiveWorkoutNotificationColor())
            return notification
        }
    }

}