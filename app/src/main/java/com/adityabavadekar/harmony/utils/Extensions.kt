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

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import com.adityabavadekar.harmony.HarmonyApplication
import com.adityabavadekar.harmony.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Application.asHarmonyApp(): HarmonyApplication {
    return this as HarmonyApplication
}

fun Context.showErrorDialog(title: String, message: String, onDismissed: () -> Unit = {}) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setIcon(R.drawable.error_octagon)
        .setPositiveButton(R.string.ok) { _, _ -> }
        .setOnDismissListener { onDismissed() }
        .create()
        .show()
}

fun runIOThread(job: CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { job() }

fun runMainThread(job: CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.Main).launch { job() }

suspend fun <T> CoroutineScope.withIOContext(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.IO, block = block)

fun String.capFirstChar() = this.replaceFirstChar { it.uppercaseChar() }