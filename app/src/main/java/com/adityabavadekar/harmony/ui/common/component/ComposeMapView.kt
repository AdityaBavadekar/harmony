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

package com.adityabavadekar.harmony.ui.common.component

import android.content.Context
import android.preference.PreferenceManager
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View.OnGenericMotionListener
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.adityabavadekar.harmony.ui.common.component.map.rememberMapViewWithLifecycle
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay


fun initialiseMapView(applicationContext: Context) {
    Configuration.getInstance().load(
        applicationContext,
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    )
}

/**
 * A composable Google Map.
 * https://gist.github.com/ArnyminerZ/418683e3ef43ccf1268f9f62940441b1
 * @author Arnau Mora
 * @since 20211230
 * @param modifier Modifiers to apply to the map.
 * @param onLoad This will get called once the map has been loaded.
 */
@Composable
fun ComposeMapView(
    modifier: Modifier = Modifier,
    mapViewState: MapView = rememberMapViewWithLifecycle(),
    onLoad: ((map: MapView) -> Unit)? = null
) {
    val context = LocalContext.current
    AndroidView(
        { mapViewState },
        modifier
    ) { mapView ->
        val copyrightOverlay = CopyrightOverlay(context)
        mapView.overlays.add(copyrightOverlay)

        mapView.setOnGenericMotionListener(
            OnGenericMotionListener { _, event ->
                /**
                 * mouse wheel zooming ftw
                 * http://stackoverflow.com/questions/11024809/how-can-my-view-respond-to-a-mousewheel
                 */
                if (0 != (event.source and InputDevice.SOURCE_CLASS_POINTER)) {
                    when (event.action) {
                        MotionEvent.ACTION_SCROLL -> {
                            if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) mapView.controller
                                .zoomOut()
                            else {
                                //this part just centers the map on the current mouse location before the zoom action occurs
                                val iGeoPoint: IGeoPoint = mapView.getProjection()
                                    .fromPixels(event.x.toInt(), event.y.toInt())
                                mapView.controller.animateTo(iGeoPoint)
                                mapView.controller.zoomIn()
                            }
                            return@OnGenericMotionListener true
                        }
                    }
                }
                false
            }
        )

        //needed for pinch zooms
        mapView.setMultiTouchControls(true);

        onLoad?.invoke(mapView)
    }
}

@Preview
@Composable
private fun ComposeMapViewPrev() {
    HarmonyTheme {
        initialiseMapView(LocalContext.current.applicationContext)
        ComposeMapView(Modifier.fillMaxSize()) {
            it.controller.setZoom(9.5)
        }
    }
}