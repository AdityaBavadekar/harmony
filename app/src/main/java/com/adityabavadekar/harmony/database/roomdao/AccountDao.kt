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
import androidx.room.Update
import com.adityabavadekar.harmony.data.model.UserRecord

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts_table LIMIT 1")
    fun getAccount(): UserRecord

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: UserRecord)

    @Update
    suspend fun updateAccount(account: UserRecord)

}