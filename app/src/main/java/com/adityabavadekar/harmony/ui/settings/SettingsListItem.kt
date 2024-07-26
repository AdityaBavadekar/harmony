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

package com.adityabavadekar.harmony.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ThemePreviews
import org.jetbrains.annotations.Debug.Renderer

internal fun LazyListScope.settingsGroupItem(
    @StringRes textRes: Int,
    content: @Composable ColumnScope.() -> Unit,
) {
    item {
        Column(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(horizontal = 18.dp)) {
                Text(
                    text = stringResource(id = textRes),
                    modifier = Modifier.padding(
                        top = 14.dp,
                        start = 8.dp,
                        bottom = 8.dp,
                        end = 8.dp,
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            content()
            HorizontalDivider()
        }
    }
}

@Composable
internal fun SettingsListTextItem(
    icon: ImageVector? = null,
    primaryText: String,
    secondaryText: String? = null,
    overlineText: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    disabled: Boolean = false,
) {

    var iconRenderer: (@Composable () -> Unit)? = null

    if (icon != null) {
        iconRenderer = { Icon(imageVector = icon, contentDescription = null) }
    }

    SettingsListTextItem(
        iconRenderer = iconRenderer,
        primaryText = primaryText,
        secondaryText = secondaryText,
        overlineText = overlineText,
        trailingContent = trailingContent,
        onClick = onClick,
        disabled = disabled
    )
}

@Composable
internal fun SettingsListTextItem(
    iconRenderer: (@Composable () -> Unit)?,
    primaryText: String,
    secondaryText: String? = null,
    overlineText: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    disabled: Boolean = false,
) {

    SettingsListItem(
        icon = iconRenderer,
        primaryContent = {
            Text(
                modifier = Modifier.alpha(SettingsListItemDefaults.titleTextAlpha),
                text = primaryText,
                style = MaterialTheme.typography.titleMedium
            )
        },
        secondaryContent = {
            secondaryText?.let {
                Text(
                    modifier = Modifier.alpha(SettingsListItemDefaults.subtitleTextAlpha),
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        overlineContent = {
            overlineText?.let {
                Text(
                    modifier = Modifier.alpha(SettingsListItemDefaults.subtitleTextAlpha),
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        trailingContent = trailingContent,
        onClick = onClick,
        disabled = disabled
    )
}

@Composable
internal fun SettingsListItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    primaryContent: @Composable () -> Unit,
    secondaryContent: (@Composable () -> Unit)? = null,
    overlineContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    disabled: Boolean = false,
) {
    Row(
        Modifier
            .heightIn(min = SettingsListItemDefaults.minHeight)
            .fillMaxWidth()
            .then(
                if (disabled) {
                    Modifier
                        .alpha(SettingsListItemDefaults.disabledItemAlpha)
                        .disabledGestures()
                } else {
                    if (onClick != null)
                        Modifier
                            .clickableRipple(onClick = onClick)
                    else Modifier
                }
            )
            .background(SettingsListItemDefaults.backgroundColor())
            .padding(
                horizontal = SettingsListItemDefaults.itemPaddingHorizontal,
                vertical = SettingsListItemDefaults.itemPaddingVertical,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Box(
                Modifier
                    .align(Alignment.Top)
                    .padding(
                        end = SettingsListItemDefaults.iconBoxPaddingEnd,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(SettingsListItemDefaults.iconSize),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                Modifier.alpha(SettingsListItemDefaults.titleTextAlpha)
            ) {
                primaryContent()
            }
            if (secondaryContent != null) {
                Column(Modifier.alpha(SettingsListItemDefaults.subtitleTextAlpha)) {
                    secondaryContent()
                }
            }
        }
        if (trailingContent != null) {
            Box(
                Modifier
                    .align(Alignment.Top)
                    .padding(
                        start = SettingsListItemDefaults.trailingBoxPaddingStart,
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier/*
                        .padding(
                            end = SettingsListItemDefaults.iconPaddingEnd,
                            bottom = SettingsListItemDefaults.iconPaddingBottom
                        )*/,
                    contentAlignment = Alignment.TopCenter
                ) {
                    trailingContent()
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SettingsListItemsPreview() {
    val checked = remember { mutableStateOf(false) }
    fun onCheckedChange() {
        checked.value = !checked.value
    }

    HarmonyTheme {
        Surface {
            LazyColumn(
                modifier = Modifier.padding(),
                contentPadding = PaddingValues()
            ) {
                settingsGroupItem(textRes = R.string.search) {
                    SettingsListTextItem(primaryText = "Title", onClick = {})
                    SettingsListTextItem(
                        icon = HarmonyIcons.Fitbit,
                        primaryText = "Title",
                        onClick = {})
                    SettingsListTextItem(
                        primaryText = "Title",
                        secondaryText = "Sub-Title",
                        onClick = {})
                    SettingsListTextItem(
                        icon = HarmonyIcons.Fitbit,
                        primaryText = "Title",
                        secondaryText = "Sub-Title",
                        onClick = { onCheckedChange() },
                        trailingContent = {
                            Switch(
                                checked = checked.value,
                                onCheckedChange = { onCheckedChange() })
                        })
                    SettingsListTextItem(
                        icon = HarmonyIcons.Fitbit,
                        primaryText = "Title",
                        secondaryText = "Sub-Title",
                        disabled = true,
                        trailingContent = {
                            Checkbox(
                                checked = checked.value,
                                onCheckedChange = { onCheckedChange() })
                        })
                    SettingsListTextItem(
                        icon = HarmonyIcons.Fitbit,
                        primaryText = "Title",
                        secondaryText = "Sub-Title",
                        onClick = { onCheckedChange() },
                        trailingContent = {
                            Checkbox(
                                checked = !checked.value,
                                onCheckedChange = { onCheckedChange() })
                        })
                    SettingsListTextItem(
                        icon = HarmonyIcons.Edit,
                        primaryText = "Three line list item",
                        secondaryText = "Secondary text that is long and perhaps goes onto another line",
                        onClick = { onCheckedChange() },
                        trailingContent = {
                            Checkbox(
                                checked = checked.value,
                                onCheckedChange = { onCheckedChange() })
                        })
                }
                settingsGroupItem(textRes = R.string.activity) {
                    SettingsListTextItem(primaryText = "Title", onClick = {})
                }
            }
        }
    }
}

//https://stackoverflow.com/questions/69142209/jetpack-compose-how-to-disable-gesture-detection-on-children
fun Modifier.disabledGestures(disabled: Boolean = true): Modifier {
    return if (disabled) {
        pointerInput(Unit) {
            awaitPointerEventScope {
                // we should wait for all new pointer events
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                        .changes
                        .forEach(PointerInputChange::consume)
                }
            }
        }
    } else {
        this
    }
}

private object SettingsListItemDefaults {
    val minHeight: Dp = 56.dp
    val titleTextAlpha: Float = 1f
    val subtitleTextAlpha: Float = 0.9f
    val disabledItemAlpha: Float = 0.6f
    val itemPaddingHorizontal: Dp = 28.dp
    val itemPaddingVertical: Dp = 16.dp
    val iconBoxPaddingEnd = 16.dp
    val trailingBoxPaddingStart = 16.dp
    val iconSize = 40.dp

    @Composable
    fun backgroundColor(): Color = MaterialTheme.colorScheme.surface
}