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

package com.adityabavadekar.harmony.data

data class TrackingConfig(
    val showStepCounter: Boolean = true,
    val showCaloriesCounter: Boolean = true,
    val showDistance: Boolean = true,
    val showSpeed: Boolean = true,
    val showMap: Boolean = true,
    val autoPauseEnabled: Boolean = true,
) {
    class Builder {
        private var showStepCounter = true
        private var showCaloriesCounter = true
        private var showDistance = true
        private var showSpeed = true
        private var showMap = true
        private var autoPauseEnabled = true

        fun noStepCounter() = apply { this.showStepCounter = false }
        fun noCaloriesCounter() = apply { this.showCaloriesCounter = false }
        fun noDistance() = apply { this.showDistance = false }
        fun noSpeed() = apply { this.showSpeed = false }
        fun noMap() = apply { this.showMap = false }
        fun disableAutoPause() = apply { this.autoPauseEnabled = false }

        fun build() = TrackingConfig(
            showStepCounter,
            showCaloriesCounter,
            showDistance,
            showSpeed,
            showMap,
            autoPauseEnabled
        )
    }

    companion object {
        fun builder() = TrackingConfig.Builder()
        fun showAll() = TrackingConfig()
        fun sedentary() = builder().noMap().noSpeed().noDistance().build()
        fun noMap() = builder().noMap().build()
    }
}
