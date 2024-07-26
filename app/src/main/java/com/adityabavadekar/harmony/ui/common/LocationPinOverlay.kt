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

package com.adityabavadekar.harmony.ui.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import androidx.core.graphics.drawable.toBitmap
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.livetracking.GeoLocation
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay

class LocationPinOverlay(
    private val view: MapView,
    private val location: GeoLocation,
) : Overlay() {

    private val locationMarker = view.resources.getDrawable(R.drawable.location_pin)
    private var mCirclePaint: Paint = Paint()
    private val mDrawPixel = Point()
    private val mPaint = Paint()
    private val navigationBitmap = view.resources.getDrawable(R.drawable.navigation_dir).toBitmap()
    private val locationPinBitmap = view.resources.getDrawable(R.drawable.location_pin).toBitmap()

    init {
        mCirclePaint.setARGB(0, 100, 100, 255)
        mCirclePaint.isAntiAlias = true
    }


    override fun draw(pCanvas: Canvas?, pProjection: Projection?) {
        if (pCanvas != null && pProjection != null) {
            drawMyLocation(pCanvas, pProjection)
        }
    }

    private fun drawMyLocation(canvas: Canvas, projection: Projection) {
        projection.toPixels(location.geoPoint, mDrawPixel)

        val radius: Float = (location.accuracy
                / TileSystem.GroundResolution(
            location.latitude, projection.zoomLevel
        ).toFloat())

        mCirclePaint.alpha = 50
        mCirclePaint.style = Paint.Style.FILL
        canvas.drawCircle(mDrawPixel.x.toFloat(), mDrawPixel.y.toFloat(), radius, mCirclePaint)

        mCirclePaint.alpha = 150
        mCirclePaint.style = Paint.Style.STROKE
        canvas.drawCircle(mDrawPixel.x.toFloat(), mDrawPixel.y.toFloat(), radius, mCirclePaint)

        //https://stackoverflow.com/a/44574931
        /*
        * Get the bearing, in degrees.
        * Bearing is the horizontal direction of travel of this device,
        * and is not related to the device orientation.
        * It is guaranteed to be in the range (0.0, 360.0] if the device has a bearing.
        * */
        val rotateAllowed = false

        if (location.hasHorizontalDisplacement() && rotateAllowed) {
            canvas.save()
            // Rotate the icon if we have a GPS fix, take into account if the map is already rotated
            var mapRotation: Float = location.requireHorizontalDisplacement
            if (mapRotation >= 360.0f) mapRotation -= 360f
            canvas.rotate(mapRotation, mDrawPixel.x.toFloat(), mDrawPixel.y.toFloat())
            // Draw the bitmap
            canvas.drawBitmap(
                navigationBitmap,
                mDrawPixel.x.toFloat(),
                mDrawPixel.y.toFloat(),
                mPaint
            )
            canvas.restore()
        } else {
            canvas.save()
            // Un-rotate the icon if the maps are rotated so the little man stays upright
            canvas.rotate(
                -view.mapOrientation, mDrawPixel.x.toFloat(),
                mDrawPixel.y.toFloat()
            )
            // Draw the bitmap
            canvas.drawBitmap(
                navigationBitmap,//locationPinBitmap,
                mDrawPixel.x.toFloat(),
                mDrawPixel.y.toFloat(),
                mPaint
            )
            canvas.restore()
        }

    }

    companion object {
        private const val CONST_RADIUS = 30f
    }

}