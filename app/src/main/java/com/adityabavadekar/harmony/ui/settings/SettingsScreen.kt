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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.model.ThirdDegreeUserRecord
import com.adityabavadekar.harmony.ui.common.component.HarmonyTopAppBar
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ImageAvatar

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onLogoutClicked: () -> Unit = {},
    onShouldNavigateBack: () -> Unit = {},
) {
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
                    primaryText = "Theme",
                    secondaryText = "Choose your light or dark theme",
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "Notifications",
                    onClick = {}
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
                    primaryText = "Sign out",
                    onClick = onLogoutClicked
                )
            }
            settingsGroupItem(R.string.about) {
                SettingsListTextItem(
                    primaryText = "Help",
                    secondaryText = "Find answers to your Harmony questions",
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "Send feedback",
                    secondaryText = "Help us improve Harmony",
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "Terms of Service",
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "Privacy Policy",
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "Open source licenses",
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "App version",
                    secondaryText = "1.0.0",
                    onClick = {}
                )
                SettingsListTextItem(
                    primaryText = "Created by Aditya Bavadekar"
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