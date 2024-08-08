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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.navigation.TopLevelDestination
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ThemePreviews

data class BottomNavigationItemInfo<T>(
    val id: T,
    val name: String,
    @DrawableRes val iconRes: Int,
)

private val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

@Composable
fun RowScope.HarmonyBottomNavigationItems(
    modifier: Modifier = Modifier,
    defaultSelectedIndex: Int = 0,
    destinations: List<TopLevelDestination>,
    onSelectionChanged: (TopLevelDestination) -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(defaultSelectedIndex) }

    destinations.forEachIndexed { index, item ->
        HarmonyNavigationBarItem(
            isSelected = index == selectedIndex,
            onClick = {
                selectedIndex = index
                onSelectionChanged(item)
            },
            icon = {
                Icon(
                    imageVector = item.unselectedIcon,
                    contentDescription = stringResource(id = item.iconTextId)
                )
            },
            selectedIcon = {
                Icon(
                    imageVector = item.selectedIcon,
                    contentDescription = stringResource(id = item.iconTextId)
                )
            },
            label = { Text(text = stringResource(id = item.titleTextId)) }
        )
    }
}

@Composable
fun RowScope.HarmonyNavigationBarItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = { if (!isSelected) onClick() },
        icon = if (isSelected) selectedIcon else icon,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = HarmonyNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = HarmonyNavigationDefaults.navigationContentColor(),
            selectedTextColor = HarmonyNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = HarmonyNavigationDefaults.navigationContentColor(),
            indicatorColor = HarmonyNavigationDefaults.navigationIndicatorColor(),
        )
    )
}


@Composable
fun HarmonyNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = HarmonyNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content
    )
}

@ThemePreviews
@Composable
private fun HarmonyNavigationBarPreview() {
    val items = listOf("Home", "Activity", "Profile")
    val icons = listOf(
        HarmonyIcons.Analytics,
        HarmonyIcons.ViewActivity,
        HarmonyIcons.AccountCircle,
    )
    val selectedIcons = icons

    HarmonyTheme {
        HarmonyNavigationBar {
            items.forEachIndexed { index, item ->
                HarmonyNavigationBarItem(
                    isSelected = index == 0,
                    onClick = {},
                    icon = { Icon(icons[index], contentDescription = item) },
                    selectedIcon = { Icon(selectedIcons[index], contentDescription = item) },
                    label = { Text(text = item) }
                )
            }
        }
    }
}

/**
 * Harmony navigation default values.
 */
object HarmonyNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
