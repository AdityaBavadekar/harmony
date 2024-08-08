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

package com.adityabavadekar.harmony.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
    val showTopAppBar: Boolean = true,
    val centeredTitle: Boolean = false,
) {
    HOME(
        selectedIcon = HarmonyIcons.Analytics,
        unselectedIcon = HarmonyIcons.Analytics,
        iconTextId = R.string.home_icon_description,
        titleTextId = R.string.home,
        centeredTitle = true
    ),
    ACTIVITY(
        selectedIcon = HarmonyIcons.Analytics,
        unselectedIcon = HarmonyIcons.Analytics,
        iconTextId = R.string.activity_icon_description,
        titleTextId = R.string.activity,
        centeredTitle = true
    ),
    PROFILE(
        selectedIcon = HarmonyIcons.AccountCircle,
        unselectedIcon = HarmonyIcons.AccountCircle,
        iconTextId = R.string.profile_icon_description,
        titleTextId = R.string.profile,
        centeredTitle = true
    )
}