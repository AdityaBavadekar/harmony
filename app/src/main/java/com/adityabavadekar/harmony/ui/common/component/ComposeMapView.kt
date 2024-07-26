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

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.adityabavadekar.harmony.ui.common.LocationPinOverlay
import com.adityabavadekar.harmony.ui.common.component.map.rememberMapViewWithLifecycle
import com.adityabavadekar.harmony.ui.livetracking.GeoLocation
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
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
@SuppressLint("ClickableViewAccessibility")
@Composable
fun ComposeMapView(
    modifier: Modifier = Modifier,
    point: GeoLocation = GeoLocation(0.00, 0.00),
    disableTouchControls: Boolean = false,
    initialize: Boolean = true,
    mapViewState: MapView = rememberMapViewWithLifecycle(),
    onLoad: ((map: MapView) -> Unit)? = null,
) {
    val context = LocalContext.current

    if (initialize) initialiseMapView(context.applicationContext)

    AndroidView(
        { mapViewState },
        modifier
    ) { mapView ->
        val copyrightOverlay = CopyrightOverlay(context)
        mapView.overlays.clear()
        mapView.overlays.add(0, copyrightOverlay)

        if (disableTouchControls) {
            mapView.setOnTouchListener { _, _ -> true }
        }
//        val myLocationNewOverlay = MyLocationNewOverlay(mapView)
//        val startMarker = Marker(mapView)
//        startMarker.icon = mapView.resources.getDrawable(R.drawable.location_pin)
//        startMarker.position = point
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//        mapView.overlays.add(1, startMarker)
//        mapView.overlays.add(myLocationNewOverlay)
        val overlay = LocationPinOverlay(mapView, location = point)
        mapView.overlays.add(1, overlay)
        mapView.controller.setCenter(point.geoPoint)
        mapView.controller.animateTo(point.geoPoint, 19.00, 200)

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