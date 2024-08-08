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

package com.adityabavadekar.harmony.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adityabavadekar.harmony.data.model.UserRecord
import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.data.model.WorkoutSummaryRecord
import com.adityabavadekar.harmony.database.roomdao.AccountDao
import com.adityabavadekar.harmony.database.roomdao.WorkoutsDao

@TypeConverters(
    value = [
        //WorkoutRecord related TypeConverter
        com.adityabavadekar.harmony.database.converter.WorkoutLapListTypeConverter::class,
        com.adityabavadekar.harmony.database.converter.WorkoutRouteTypeConverter::class,
        com.adityabavadekar.harmony.database.converter.WorkoutTypesTypeConverter::class,
        com.adityabavadekar.harmony.database.converter.DoubleListTypeConverter::class,

        //UserRecord related TypeConverter
        com.adityabavadekar.harmony.database.converter.UserFitnessRecordTypeConverter::class,
        com.adityabavadekar.harmony.database.converter.AchievementsTypeListTypeConverter::class,
        com.adityabavadekar.harmony.database.converter.AvatarTypeTypeConverter::class,
        com.adityabavadekar.harmony.database.converter.GenderTypeConverter::class,
    ]
)
@Database(
    entities = [
        WorkoutRecord::class,
        UserRecord::class
    ],
    views = [
        WorkoutSummaryRecord::class
    ],
    exportSchema = false,
    version = 1
)
abstract class HarmonyRoomDatabase : RoomDatabase() {

    abstract fun workoutsDao(): WorkoutsDao

    abstract fun accountDao(): AccountDao

    companion object {
        private const val DATABASE_NAME = "harmony_app_database"

        /** The value of a volatile variable is never cached, and all reads and writes are to and from the main memory. */
        @Volatile
        private var instance: HarmonyRoomDatabase? = null

        /**
         * Multiple threads can potentially ask for a database instance at the same time, which results
         * in two databases instead of one. This issue is known as a race condition.
         * Wrapping the code to get the database inside a synchronized block means that only one thread
         * of execution at a time can enter this block of code, which makes sure the database only
         * gets initialized once.
         * Use synchronized{} block to avoid the race condition.
         * */
        fun getDatabase(context: Context): HarmonyRoomDatabase {
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(
                    context,
                    HarmonyRoomDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance!!
            }
        }

    }

}