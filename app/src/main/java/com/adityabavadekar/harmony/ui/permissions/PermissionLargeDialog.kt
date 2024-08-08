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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.icon.HarmonyIcons

@Composable
private fun PermissionLargeDialog(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    @StringRes extraInformationTextRes: Int? = null,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    verticalLayoutStyle: Boolean = true
) {
    @Composable
    fun parentLayout(content: @Composable () -> Unit) {
        if (verticalLayoutStyle) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { content() }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) { content() }
        }
    }

    val textAlign: TextAlign? = if (verticalLayoutStyle) TextAlign.Center else null

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
                parentLayout {
                    Box(
                        Modifier
                            .padding(18.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        Row(Modifier.padding(8.dp)) {
                            Icon(
                                modifier = Modifier.size(42.dp),
                                imageVector = icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
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
                    textAlign = textAlign
                )
                if (extraInformationTextRes != null) {
                    VerticalSpacer(size = 16.dp)
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(0.08f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .alpha(0.8f),
                            text = stringResource(id = extraInformationTextRes),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = textAlign
                        )
                    }
                }
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
                    androidx.compose.material3.Text(text = "Allow")
                }
            }
        }
    }
}

@Preview
@Composable
fun LocationPermissionDialogContent(
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
) {
    PermissionLargeDialog(
        icon = HarmonyIcons.MyLocation,
        titleRes = R.string.location_permission_dialog_title,
        descriptionRes = R.string.location_permission_dialog_description,
        extraInformationTextRes = R.string.location_permission_dialog_extra_information_text,
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick
    )
}


@Preview
@Composable
fun ActivityRecognitionPermissionDialogContent(
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
) {
    PermissionLargeDialog(
        icon = HarmonyIcons.Run,
        titleRes = R.string.activity_recognition_permission_dialog_title,
        descriptionRes = R.string.activity_recognition_permission_dialog_description,
        extraInformationTextRes = R.string.activity_recognition_permission_dialog_extra_information_text,
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick
    )
}

@Preview
@Composable
fun NotificationPermissionDialogContent(
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
) {
    PermissionLargeDialog(
        icon = HarmonyIcons.Notification,
        titleRes = R.string.notification_permission_dialog_title,
        descriptionRes = R.string.notification_permission_dialog_description,
        extraInformationTextRes = R.string.notification_permission_dialog_extra_information_text,
        onPositiveButtonClick = onPositiveButtonClick,
        onNegativeButtonClick = onNegativeButtonClick
    )
}