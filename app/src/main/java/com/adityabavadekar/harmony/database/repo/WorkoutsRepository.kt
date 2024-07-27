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

package com.adityabavadekar.harmony.database.repo

import com.adityabavadekar.harmony.data.model.WorkoutRecord
import com.adityabavadekar.harmony.database.repobase.BaseWorkoutsRepository
import com.adityabavadekar.harmony.database.roomdao.WorkoutsDao

class WorkoutsRepository(
    private val dao: WorkoutsDao,
) : BaseWorkoutsRepository {
    override fun getAll(): List<WorkoutRecord> {
        return dao.getAll()
    }

    override fun getWorkoutRecord(recordId: Int): WorkoutRecord? {
        return dao.getWorkoutRecord(recordId)
    }

    override fun getIncompleteWorkoutRecord(): WorkoutRecord? {
        return dao.getIncompleteWorkoutRecord()
    }

    override suspend fun insertWorkoutRecord(record: WorkoutRecord) {
        dao.insertWorkoutRecord(record)
    }

    override suspend fun updateWorkoutRecord(record: WorkoutRecord) {
        dao.updateWorkoutRecord(record)
    }

    companion object {
        private var instance: WorkoutsRepository? = null

        fun getInstance(workoutsDao: WorkoutsDao): WorkoutsRepository {
            return instance ?: synchronized(this) {
                instance = WorkoutsRepository(workoutsDao)
                instance!!
            }
        }
    }

}