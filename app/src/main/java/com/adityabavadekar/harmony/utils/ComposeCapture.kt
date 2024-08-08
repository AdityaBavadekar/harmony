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

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView

class ComposeCaptureState {

    val bitmapState = mutableStateOf<Bitmap?>(null)

    internal var callback: (() -> Unit)? = null

    /**
     * Screenshot will be stored in [bitmap]
     * */
    fun capture() {
        callback?.invoke()
    }

    val bitmap: Bitmap?
        get() = bitmapState.value

}

@Composable
fun rememberComposeCaptureState(): ComposeCaptureState {
    return remember {
        ComposeCaptureState()
    }
}

private fun View.screenshot(
    bounds: Rect,
    callback: (Bitmap?) -> Unit
) {

    try {

        val bitmap = Bitmap.createBitmap(
            bounds.width.toInt(),
            bounds.height.toInt(),
            Bitmap.Config.ARGB_8888,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Above Android O not using PixelCopy throws exception
            // https://stackoverflow.com/questions/58314397/java-lang-illegalstateexception-software-rendering-doesnt-support-hardware-bit
            PixelCopy.request(
                (this.context as Activity).window,
                android.graphics.Rect(
                    bounds.left.toInt(),
                    bounds.top.toInt(),
                    bounds.right.toInt(),
                    bounds.bottom.toInt()
                ),
                bitmap,
                {},
                Handler(Looper.getMainLooper())
            )
        } else {
            val canvas = Canvas(bitmap)
                .apply {
                    translate(-bounds.left, -bounds.top)
                }
            this.draw(canvas)
            canvas.setBitmap(null)
        }
        callback(bitmap)
    } catch (e: Exception) {
        Log.e("View.screenshot", "Error!!", e)
        return callback(null)
    }
}


@Composable
fun ComposeCapture(
    captureControllerState: ComposeCaptureState,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)
) {

    val view: View = LocalView.current

    val composableBounds = remember {
        mutableStateOf<Rect?>(null)
    }

    DisposableEffect(Unit) {

        captureControllerState.callback = {
            composableBounds.value?.let { bounds ->
                if (bounds.width == 0f || bounds.height == 0f) return@let

                view.screenshot(bounds) { bitmap ->
                    if (bitmap != null) {
                        captureControllerState.bitmapState.value = bitmap
                    }
                }
            }
        }

        onDispose {
            val bmp = captureControllerState.bitmapState.value
            bmp?.apply {
                if (!isRecycled) {
                    recycle()
                }
            }
            captureControllerState.bitmapState.value = null
            captureControllerState.callback = null
        }
    }

    Box(modifier = modifier
        .onGloballyPositioned {
            composableBounds.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.boundsInWindow()
            } else {
                it.boundsInRoot()
            }
        }
    ) {
        content()
    }
}

