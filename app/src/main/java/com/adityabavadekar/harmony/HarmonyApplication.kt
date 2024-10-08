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

package com.adityabavadekar.harmony

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.adityabavadekar.harmony.database.HarmonyRoomDatabase
import com.adityabavadekar.harmony.database.repo.AccountRepository
import com.adityabavadekar.harmony.database.repo.WorkoutsRepository
import com.adityabavadekar.harmony.ui.livetracking.service.LiveTrackerService

class HarmonyApplication : Application() {

    private lateinit var database: HarmonyRoomDatabase

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Application")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels()
            Log.d(TAG, "onCreate: NotificationChannels Created")
        }

        database = HarmonyRoomDatabase.getDatabase(this)
        Log.d(TAG, "onCreate: Database initialised")
    }

    fun getDatabase() = database

    fun getAccountRepository(): AccountRepository {
        return AccountRepository.getInstance(database.accountDao())
    }

    fun getWorkoutRepository(): WorkoutsRepository {
        return WorkoutsRepository.getInstance(database.workoutsDao())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels() {
        val locationServiceChannel = NotificationChannel(
            LiveTrackerService.LOCATION_NOTIFICATION_CHANNEL_ID,
            "Location notifier service",
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(locationServiceChannel)
    }

    companion object {
        private const val TAG = "[HarmonyApplication]"
    }

}