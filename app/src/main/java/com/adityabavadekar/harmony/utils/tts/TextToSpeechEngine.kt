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

package com.adityabavadekar.harmony.utils.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import java.util.Random

/**
 * Remember to call [destroy] to free resources
 * */
class TextToSpeechEngine(private val context: Context) : TextToSpeech.OnInitListener {

    private val maxLength = TextToSpeech.getMaxSpeechInputLength()
    private val engine: TextToSpeech = TextToSpeech(context, this)
    private var initialised: Boolean = false

    override fun onInit(status: Int) {
        val isSuccess = status == TextToSpeech.SUCCESS
        Log.d(TAG, "onInit: isSuccess=$isSuccess")

        if (!isSuccess) {
            return
        }

        initialised = true
        engine.setLanguage(Locale.getDefault())

    }

    fun speak(text: String, immediate: Boolean = true) {
        if (!initialised) {
            Log.e(TAG, "speak: SpeechEngine not initialised successfully!!")
            return
        }

        if (text.trim().isEmpty()) return

        if (text.length > maxLength) {
            Log.w(
                TAG, "speak: WARNING Text sequence provided is longer than max length " +
                        "[MAX=$maxLength][PROVIDED=${text.length}]"
            )
        }

        val isSuccess = engine.speak(
            text, /* Text sequence */
            if (immediate) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, /* Queue mode */
            null, /* Params for Speech Engine */
            Random().nextLong().toString() /* UtteranceId */
        )
        if (isSuccess == TextToSpeech.SUCCESS) {
            Log.i(TAG, "speak: Utterance Success")
        } else {
            Log.e(TAG, "speak: Error! status during utterance!")
        }
    }

    /**
     * Releases the resources used by the TextToSpeech engine.
     * It is good practice for instance to call this method in the onDestroy() method of
     * an Activity so the TextToSpeech engine can be cleanly stopped.
     * */
    fun destroy() {
        Log.d(TAG, "destroy")
        initialised = false
        engine.shutdown()
    }

    companion object {
        private const val TAG = "[TextToSpeechEngine]"
    }
}