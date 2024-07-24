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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.adityabavadekar.harmony.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesManager private constructor(context: Context) {
    fun putString(key: PreferencesKeys, value: String?) {
        editAsync { putString(key.name, value) }
    }

    fun putInt(key: PreferencesKeys, value: Int) {
        editAsync { putInt(key.name, value) }
    }

    fun putLong(key: PreferencesKeys, value: Long) {
        editAsync { putLong(key.name, value) }
    }

    fun putFloat(key: PreferencesKeys, value: Float) {
        editAsync { putFloat(key.name, value) }
    }

    fun putBoolean(key: PreferencesKeys, value: Boolean) {
        editAsync { putBoolean(key.name, value) }
    }

    fun remove(key: PreferencesKeys) {
        editAsync { remove(key.name) }
    }

    val all: MutableMap<String, *>
        get() {
            return sharedPreferences.all
        }

    fun getString(key: PreferencesKeys, default: String? = null): String? {
        return sharedPreferences.getString(key.name, default)
    }

    fun getInt(key: PreferencesKeys, default: Int? = null): Int? {
        return if (has(key)) {
            sharedPreferences.getInt(key.name, 0)
        } else default
    }

    fun getLong(key: PreferencesKeys, default: Long? = null): Long? {
        return if (has(key)) {
            sharedPreferences.getLong(key.name, 0L)
        } else default
    }

    fun getFloat(key: PreferencesKeys, default: Float?): Float? {
        return if (has(key)) {
            sharedPreferences.getFloat(key.name, 0f)
        } else default
    }

    fun getBoolean(key: PreferencesKeys, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key.name, default)
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(DEFAULT_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun has(key: PreferencesKeys) = sharedPreferences.contains(key.name)

    fun get(key: PreferencesKeys, default: String? = null): String? = getString(key, default)
    fun get(key: PreferencesKeys, default: Int? = null): Int? = getInt(key, default)
    fun get(key: PreferencesKeys, default: Float? = null): Float? = getFloat(key, default)
    fun get(key: PreferencesKeys, default: Long? = null): Long? = getLong(key, default)
    fun get(key: PreferencesKeys, default: Boolean): Boolean = getBoolean(key, default)

    fun set(key: PreferencesKeys, value: String?) {
        sharedPreferences.edit {
            putString(key.name, value)
        }
    }

    fun editAsync(editValues: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit {
            editValues(this)
        }
    }

    companion object {
        private const val DEFAULT_SHARED_PREFERENCES_NAME = "default_prefs"
        fun getInstance(context: Context): PreferencesManager {
            return PreferencesManager(context)
        }

        fun keys() = PreferencesKeys.entries.map { it.name }

    }
}

val Context.preferencesManager: PreferencesManager
    get() {
        return PreferencesManager.getInstance(this)
    }