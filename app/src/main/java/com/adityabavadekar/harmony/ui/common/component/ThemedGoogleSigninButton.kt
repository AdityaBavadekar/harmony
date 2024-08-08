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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ThemePreviews

/**
 * Themed GoogleSignInButton designed by following guidelines from https://developers.google.com/identity/branding-guidelines
 * */
@Composable
fun ThemedGoogleSigninButton(
    onClick: () -> Unit = {},
    fillWidth: Boolean = false,
) {
    Button(
        modifier = Modifier.then(
            if (fillWidth) Modifier.fillMaxWidth()
            else Modifier
        ),
        colors = ButtonColors(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onSurface,
        ),
        contentPadding = PaddingValues(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
        onClick = onClick
    ) {
        Row(
            Modifier
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = com.google.android.gms.base.R.drawable.googleg_standard_color_18),
                contentDescription = "Google Logo"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Sign in with Google",
                style = MaterialTheme.typography.labelLarge,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@ThemePreviews
@Composable
private fun GButtonPrev() {
    HarmonyTheme {
        Surface {
            Column(
                Modifier.padding(18.dp)
            ) {
                ThemedGoogleSigninButton()
            }
        }
    }
}

@ThemePreviews
@Composable
private fun GButtonFullPrev() {
    HarmonyTheme {
        Surface {
            Column(
                Modifier.padding(18.dp)
            ) {
                ThemedGoogleSigninButton(fillWidth = true)
            }
        }
    }
}