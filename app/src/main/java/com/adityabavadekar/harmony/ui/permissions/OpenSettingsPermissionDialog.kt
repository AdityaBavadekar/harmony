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

package com.adityabavadekar.harmony.ui.permissions

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer

@Composable
private fun OpenSettingsPermissionDialog(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
) {
    Surface(
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.padding(
                top = 28.dp,
                start = 28.dp,
                end = 28.dp,
                bottom = 18.dp,
            )
        ) {
            Column {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = titleRes),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                VerticalSpacer(size = 16.dp)
                Text(
                    modifier = Modifier.alpha(0.8f),
                    text = stringResource(id = descriptionRes),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            VerticalSpacer(size = 18.dp)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { onNegativeButtonClick() }) {
                    androidx.compose.material3.Text(text = "Deny Access")
                }
                Button(onClick = { onPositiveButtonClick() }) {
                    androidx.compose.material3.Text(text = "Open settings")
                }
            }
        }
    }
}

@Preview
@Composable
fun OpenSettingsLocationPermissionDialogContent(
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
) {
    OpenSettingsPermissionDialog(
        titleRes = R.string.open_settings_location_permission_dialog_title,
        descriptionRes = R.string.open_settings_location_permission_dialog_description,
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick
    )
}


@Preview
@Composable
fun OpenSettingsActivityRecognitionPermissionDialogContent(
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
) {
    OpenSettingsPermissionDialog(
        titleRes = R.string.open_settings_activity_permission_dialog_title,
        descriptionRes = R.string.open_settings_activity_permission_dialog_description,
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick
    )
}

@Preview
@Composable
fun OpenSettingsNotificationPermissionDialogContent(
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
) {
    OpenSettingsPermissionDialog(
        titleRes = R.string.open_settings_notification_permission_dialog_title,
        descriptionRes = R.string.open_settings_notification_permission_dialog_description,
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick
    )
}