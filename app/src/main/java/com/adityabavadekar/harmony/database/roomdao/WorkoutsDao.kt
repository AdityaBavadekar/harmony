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

package com.adityabavadekar.harmony.database.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Upsert
import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.data.model.WorkoutSummaryRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutsDao {

    @Query("SELECT * FROM workouts_table ORDER BY startTimestamp DESC")
    fun getAll(): List<WorkoutRecord>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM workouts_table ORDER BY startTimestamp DESC")
    fun getAllSummaryRecords(): Flow<List<WorkoutSummaryRecord>>

    @Query("SELECT * FROM workouts_table WHERE id=:recordId LIMIT 1")
    fun getWorkoutRecord(recordId: Int): WorkoutRecord?

    @Query("SELECT * FROM workouts_table WHERE completed = '0' ORDER BY startTimestamp DESC LIMIT 1")
    fun getIncompleteWorkoutRecord(): WorkoutRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutRecord(record: WorkoutRecord): Long

    @Upsert
    suspend fun updateWorkoutRecord(record: WorkoutRecord)
}