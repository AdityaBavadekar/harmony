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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: String,
    large: Boolean = false,
    fontWeight: FontWeight? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    padding: PaddingValues = PaddingValues(8.dp),
) {
    val parentTextStyle = MaterialTheme.typography.labelLarge
    Text(
        modifier = modifier
            .padding(padding)
            .alpha(0.9f),
        text = text,
        color = color,
        style = if (large) parentTextStyle.copy(fontSize = parentTextStyle.fontSize.value.plus(2).sp) else parentTextStyle,
        fontWeight = fontWeight ?: parentTextStyle.fontWeight
    )
}