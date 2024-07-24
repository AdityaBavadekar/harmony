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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

data class MenuAction(
    val icon: ImageVector,
    @StringRes val iconContentDescription: Int,
)

private val topAppBarMinHeight = 64.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonyTopAppBar(
    modifier: Modifier = Modifier,
    titleRes: Int,
    navigationIcon: (@Composable () -> Unit)? = null,
    onNavigationIconClicked: () -> Unit,
    actionIcons: List<MenuAction>,
    onActionIconClicked: (index: Int) -> Unit,
    centeredTitle: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    if (!centeredTitle) {

        TopAppBar(
            modifier = modifier.heightIn(min = topAppBarMinHeight),
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = titleRes),
                    style = textStyle,
                    textAlign = if (centeredTitle) TextAlign.Center else TextAlign.Start
                )
            },
            navigationIcon = {
                if (navigationIcon != null) {
                    IconButton(
                        onClick = onNavigationIconClicked,
                        content = navigationIcon
                    )
                }
            },
            actions = {
                actionIcons.forEachIndexed { index, (icon, iconContentDescription) ->
                    IconButton(onClick = { onActionIconClicked(index) }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(id = iconContentDescription),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            },
            backgroundColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            elevation = 4.dp
        )
    } else {

        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = titleRes),
                    style = textStyle
                )
            },
            navigationIcon = {
                if (navigationIcon != null) {
                    IconButton(
                        onClick = onNavigationIconClicked,
                        content = navigationIcon
                    )
                }
            },
            actions = {
                actionIcons.forEachIndexed { index, (icon, iconContentDescription) ->
                    IconButton(onClick = { onActionIconClicked(index) }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(id = iconContentDescription),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = MaterialTheme.colorScheme.surface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )
    }

}

@Preview(group = "Top App Bar")
@Composable
private fun HarmonyTopAppBarPrev() {
    HarmonyTheme {
        HarmonyTopAppBar(
            titleRes = R.string.home,
            navigationIcon = {
                Icon(
                    imageVector = HarmonyIcons.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            },
            actionIcons = listOf(
                MenuAction(HarmonyIcons.Settings, R.string.settings),
            ),
            onNavigationIconClicked = {},
            onActionIconClicked = {}
        )
    }
}

@Preview(group = "Top App Bar", name = "Centered")
@Composable
private fun HarmonyTopAppBarCenteredPrev() {
    HarmonyTheme {
        HarmonyTopAppBar(
            titleRes = R.string.home,
            navigationIcon = {
                Icon(
                    imageVector = HarmonyIcons.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            },
            actionIcons = listOf(
                MenuAction(HarmonyIcons.Settings, R.string.settings),
            ),
            onNavigationIconClicked = {},
            onActionIconClicked = {},
            centeredTitle = true
        )
    }
}

@Preview(group = "Top App Bar", name = "No Menu Action")
@Composable
private fun HarmonyTopAppBarNoMenuPrev() {
    HarmonyTheme {
        HarmonyTopAppBar(
            titleRes = R.string.home,
            navigationIcon = {
                Icon(
                    imageVector = HarmonyIcons.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            },
            actionIcons = emptyList(),
            onNavigationIconClicked = {},
            onActionIconClicked = {}
        )
    }
}

@Preview(group = "Top App Bar", name = "No Menu Action Centered")
@Composable
private fun HarmonyTopAppBarNoMenuCenteredPrev() {
    HarmonyTheme {
        HarmonyTopAppBar(
            titleRes = R.string.home,
            navigationIcon = {
                Icon(
                    imageVector = HarmonyIcons.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            },
            actionIcons = emptyList(),
            onNavigationIconClicked = {},
            onActionIconClicked = {},
            centeredTitle = true
        )
    }
}

@Preview(group = "Top App Bar", name = "Text Only")
@Composable
private fun HarmonyTopAppBarTextOnlyPrev() {
    HarmonyTheme {
        HarmonyTopAppBar(
            titleRes = R.string.home,
            actionIcons = emptyList(),
            onNavigationIconClicked = {},
            onActionIconClicked = {}
        )
    }
}

@Preview(group = "Top App Bar", name = "Text Only Centered")
@Composable
private fun HarmonyTopAppBarTextOnlyCenteredPrev() {
    HarmonyTheme {
        HarmonyTopAppBar(
            titleRes = R.string.home,
            actionIcons = emptyList(),
            onNavigationIconClicked = {},
            onActionIconClicked = {},
            centeredTitle = true
        )
    }
}