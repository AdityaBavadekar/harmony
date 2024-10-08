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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.CommonMenuActions
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

data class MenuAction(
    val icon: ImageVector,
    @StringRes val iconContentDescription: Int,
    @StringRes val text: Int,
    val type: MenuActionType = MenuActionType.ICON,
)

enum class MenuActionType {
    ICON,
    DROP_DOWN
}

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
    elevated: Boolean = false
) {
    val titleContent = @Composable {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = if (centeredTitle) TextAlign.Center else TextAlign.Start,
            text = stringResource(id = titleRes),
            style = textStyle
        )
    }
    val navigationIconContent: (@Composable () -> Unit)? =
        if (navigationIcon != null) {
            @Composable {
                IconButton(
                    onClick = onNavigationIconClicked,
                    content = navigationIcon
                )
            }
        } else null

    val actionsContent = @Composable {
        if (actionIcons.isEmpty()) {
            Box(Modifier.clickable(false) {}) {
                IconButton(enabled = false, onClick = {}, interactionSource = null) {
                }
            }
        }
        actionIcons.filter { it.type == MenuActionType.ICON }
            .forEachIndexed { index, action ->
                IconButton(onClick = { onActionIconClicked(index) }) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = stringResource(id = action.iconContentDescription),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        val dropDownActions = actionIcons.filter { it.type == MenuActionType.DROP_DOWN }
        var isDropDownOpen by remember { mutableStateOf(false) }
        DropdownMenu(
            expanded = isDropDownOpen,
            onDismissRequest = { isDropDownOpen = false }) {
            dropDownActions.forEachIndexed { index, menuAction ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = menuAction.text))
                    },
                    onClick = {
                        isDropDownOpen = false
                        onActionIconClicked(index)
                    }
                )
            }
        }
        if (dropDownActions.isNotEmpty()) {
            IconButton(onClick = {
                isDropDownOpen = true
            }) {
                Icon(
                    imageVector = HarmonyIcons.MoreVert,
                    contentDescription = stringResource(id = R.string.more_options),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val elevation = if (elevated) AppBarDefaults.TopAppBarElevation else 1.dp

    HarmonyBaseTopAppBar(
        modifier = modifier.heightIn(min = topAppBarMinHeight),
        title = titleContent,
        navigationIcon = navigationIconContent,
        actions = { actionsContent() },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    )
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonySearchInputTopAppBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
    elevated: Boolean = false
) {
    var searchText by remember { mutableStateOf("") }
    val titleContent = @Composable {
        SearchBarDefaults.InputField(
            query = searchText,
            onQueryChange = {
                searchText = it
                onSearchTextChanged(it)
            },
            onSearch = { },
            expanded = true,
            onExpandedChange = { expanded -> if (!expanded) onBackClicked() },
            placeholder = { Text("Search here") },
        )
    }

    val navigationIconContent: (@Composable () -> Unit) =
        @Composable {
            IconButton(
                onClick = onBackClicked,
            ) {
                Icon(imageVector = HarmonyIcons.ArrowBack, contentDescription = null)
            }
        }

    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val elevation = if (elevated) AppBarDefaults.TopAppBarElevation else 1.dp

    HarmonyBaseTopAppBar(
        modifier = modifier.heightIn(min = topAppBarMinHeight),
        title = titleContent,
        navigationIcon = navigationIconContent,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    )
}

@Composable
fun HarmonyBaseTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = androidx.compose.material.MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    TopAppBar(
        modifier = modifier.heightIn(min = topAppBarMinHeight),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    )
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
                MenuAction(HarmonyIcons.Settings, R.string.settings, R.string.settings),
                CommonMenuActions.settings()
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
                MenuAction(HarmonyIcons.Settings, R.string.settings, R.string.settings),
                CommonMenuActions.settings()
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
            navigationIcon = null,
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