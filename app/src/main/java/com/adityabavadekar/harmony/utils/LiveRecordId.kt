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

package com.adityabavadekar.harmony.utils

import android.content.Context
import android.util.Log
import com.adityabavadekar.harmony.utils.preferences.PreferencesKeys
import com.adityabavadekar.harmony.utils.preferences.preferencesManager

class LiveRecordId(private val context: Context) {

    private val preferencesManager = context.preferencesManager

    /**
     * Sets the LiveRecord id to given value.
     * @throws IllegalStateException If LiveRecord id was not cleared previously and holds a non-null value.
     * */
    fun set(id: Long) {
        if (id < 0) throw IllegalArgumentException("id cannot be negative. Negative id indicating null state is only internally handled here.")
        val storedId = getId()
        if (storedId == null) {
            preferencesManager.setLong(LIVE_RECORD_ID_KEY, id)
            Log.d(TAG, "set: id updated to $id")
        } else {
            Log.e(
                TAG,
                "set: Cannot set new LiveRecord id (=$id) because previously stored id (=$storedId) is not cleared"
            )
            throw IllegalStateException("LiveRecord id was not cleared previously and holds a non-null value (=$storedId)")
        }
    }

    /**
     * Clears the LiveRecord id from preferences storage.
     * */
    fun clear() {
        preferencesManager.remove(LIVE_RECORD_ID_KEY)
        Log.d(TAG, "clear: LiveRecord id cleared.")
    }

    /**
     * If the id is present returns non-null value otherwise null.
     * */
    fun getId(): Long? {
        val id = preferencesManager.getLong(LIVE_RECORD_ID_KEY, null)
        Log.i(TAG, "getId: ID=$id")
        return id
    }

    companion object {
        private val LIVE_RECORD_ID_KEY = PreferencesKeys.LIVE_RECORD_ID
        private const val TAG = "[LiveRecordId]"
    }
}