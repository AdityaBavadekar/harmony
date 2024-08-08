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

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

class ConfigurationUtils {
    companion object {
        /**
         * Landscape is an orientation where the screen width is greater than the screen height and
         * depending on platform convention locking the screen to landscape can represent
         * landscape-primary, landscape-secondary or both.
         * */
        @Composable
        fun isLandscape(configuration: Configuration = LocalConfiguration.current): Boolean {
            return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }
    }
}