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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.component.Label
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

@Preview
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Surface {
        LazyColumn(
            modifier = Modifier.padding(),
            contentPadding = PaddingValues()
        ) {
            item {
                SettingsListItem(
                    icon = HarmonyIcons.Fitbit,
                    primaryContent = {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    secondaryContent = { Text(text = "Title") },
                    trailingContent = { Icon(HarmonyIcons.Edit, contentDescription = null) })
//                SettingsGroup(textRes = R.string.search) {
//                }
            }
        }
    }
}

@Composable
fun SettingsGroup(
    @StringRes textRes: Int,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
//        Column(Modifier.padding(horizontal = 18.dp)) {
//        }
        Label(text = stringResource(id = textRes))
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsListItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    primaryContent: @Composable () -> Unit,
    secondaryContent: (@Composable () -> Unit)? = null,
    overlineContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Row(
        Modifier
            .heightIn(min = SettingsListItemDefaults.minHeight)
            .fillMaxWidth()
            .clickableRipple(onClick = onClick)
            .padding(SettingsListItemDefaults.padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(
                        end = SettingsListItemDefaults.iconBoxPaddingEnd,
                        start = SettingsListItemDefaults.iconBoxPaddingStart,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            end = SettingsListItemDefaults.iconPaddingEnd,
                            bottom = SettingsListItemDefaults.iconPaddingBottom
                        )
                ) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.alpha(SettingsListItemDefaults.titleTextAlpha),
                text = "Title",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                modifier = Modifier.alpha(SettingsListItemDefaults.subtitleTextAlpha),
                text = "Subtitle",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (trailingContent != null) {
            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(
                        start = SettingsListItemDefaults.trailingBoxPaddingStart,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            end = SettingsListItemDefaults.iconPaddingEnd,
                            bottom = SettingsListItemDefaults.iconPaddingBottom
                        )
                ) {
                    trailingContent()
                }
            }
        }
    }
}

object SettingsListItemDefaults {
    val minHeight: Dp = 56.dp
    val titleTextAlpha: Float = 1f
    val subtitleTextAlpha: Float = 0.8f
    val padding: Dp = 16.dp
    val iconPaddingEnd = 16.dp
    val iconPaddingBottom = 16.dp
    val iconBoxPaddingEnd = 8.dp
    val iconBoxPaddingStart = 8.dp
    val trailingBoxPaddingStart = 16.dp

}