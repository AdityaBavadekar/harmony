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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.adityabavadekar.harmony.BuildConfig
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.model.ThirdDegreeUserRecord
import com.adityabavadekar.harmony.ui.common.Dialogs
import com.adityabavadekar.harmony.ui.common.component.HarmonyTopAppBar
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ImageAvatar

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onLogoutClicked: () -> Unit = {},
    onShouldNavigateBack: () -> Unit = {},
    openNotificationSettings: () -> Unit = {}
) {
    var themeSelectionIsOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HarmonyTopAppBar(
                titleRes = R.string.settings,
                navigationIcon = {
                    Icon(
                        imageVector = HarmonyIcons.ArrowBack,
                        contentDescription = null
                    )
                },
                onNavigationIconClicked = onShouldNavigateBack,
                actionIcons = emptyList(),
                onActionIconClicked = {},
                centeredTitle = true
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues()
        ) {
            settingsGroupItem(R.string.general) {
                SettingsListTextItem(
                    primaryText = stringResource(R.string.theme),
                    secondaryText = stringResource(R.string.choose_your_light_or_dark_theme),
                    onClick = {
                        themeSelectionIsOpen = true
                    }
                )
                SettingsListTextItem(
                    primaryText = "Notifications",
                    onClick = { openNotificationSettings() }
                )
            }
            settingsGroupItem(R.string.account) {
                SettingsListTextItem(
                    iconRenderer = {
                        Image(
                            painter = painterResource(
                                id = uiState.thirdDegreeUserRecord.avatar?.drawableRes
                                    ?: ImageAvatar.getRandom()
                            ),
                            contentDescription = null
                        )
                    },
                    primaryText = uiState.thirdDegreeUserRecord.getName(),
                    secondaryText = uiState.thirdDegreeUserRecord.email
                )
                SettingsListTextItem(
                    icon = HarmonyIcons.SignOut,
                    primaryText = stringResource(R.string.sign_out),
                    onClick = onLogoutClicked
                )
            }
            settingsGroupItem(R.string.about) {
                SettingsListTextItem(
                    primaryText = stringResource(R.string.help),
                    secondaryText = stringResource(R.string.find_answers_to_your_harmony_questions),
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = stringResource(R.string.send_feedback),
                    secondaryText = stringResource(R.string.help_us_improve_harmony),
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = stringResource(R.string.terms_of_service),
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = stringResource(R.string.privacy_policy),
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = stringResource(R.string.open_source_licenses),
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = stringResource(R.string.app_version),
                    secondaryText = BuildConfig.VERSION_NAME,
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "Created by Aditya Bavadekar"
                )

            }
        }

        when {
            themeSelectionIsOpen -> {
                Dialogs.ThemeSelectorDialog(
                    onSelected = {
                        themeSelectionIsOpen = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPrev() {
    val uiState = SettingsUiState(
        thirdDegreeUserRecord = ThirdDegreeUserRecord.Builder("test")
            .setName("Test User")
            .setEmail("user@gmail.com")
            .setAvatar(ImageAvatar.AvatarType.MALE3)
            .build(),
    )
    HarmonyTheme {
        SettingsScreen(uiState = uiState)
    }
}