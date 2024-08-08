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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

object Dialogs {

    enum class ThemeOptions(@StringRes val nameRes: Int) {
        LIGHT_THEME(R.string.theme_light),
        DARK_THEME(R.string.theme_dark),
        SYSTEM_DEFAULT_THEME(R.string.theme_system_default)
    }

    @Composable
    fun ThemeSelectorDialog(
        defaultSelectedTheme: ThemeOptions = ThemeOptions.SYSTEM_DEFAULT_THEME,
        onSelected: (ThemeOptions) -> Unit = {}
    ) {
        var selectedTheme by remember { mutableStateOf(defaultSelectedTheme) }

        fun changeTheme(themeOptions: ThemeOptions) {
            selectedTheme = themeOptions
            onSelected(themeOptions)
        }

        Dialog(onDismissRequest = { onSelected(selectedTheme) }) {
            Surface(
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 28.dp
                    )
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .padding(end = 18.dp)
                                .alpha(0.8f)
                        ) {
                            Icon(
                                imageVector = HarmonyIcons.ThemeIndicator,
                                contentDescription = null
                            )
                        }
                        Text(
                            text = stringResource(R.string.choose_a_theme),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        ThemeOptions.entries.forEach { themeOption ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickableRipple({ changeTheme(themeOption) }),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedTheme == themeOption,
                                    onClick = { changeTheme(themeOption) }
                                )
                                Text(text = stringResource(id = themeOption.nameRes))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ThemeSelectorDialogPreview() {
    HarmonyTheme {
        Dialogs.ThemeSelectorDialog()
    }
}