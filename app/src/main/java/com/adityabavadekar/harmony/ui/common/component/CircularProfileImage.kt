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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

enum class CircularProfileImageSize(val size: Dp) {
    SMALL(60.dp),
    MEDIUM(144.dp),
    LARGE(180.dp)
}

@Composable
fun CircularProfileImage(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    @DrawableRes iconIdRes: Int? = R.drawable.account_circle,
    size: CircularProfileImageSize = CircularProfileImageSize.LARGE,
    margin: PaddingValues = PaddingValues(),
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Box(
        modifier = modifier
            .padding(margin)
            .clip(CircleShape)
            .size(size.size)
            .background(backgroundColor)
    ) {
        if (painter != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = "Profile Image"
            )
        } else if (imageVector != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                imageVector = imageVector,
                contentDescription = "Profile Image"
            )
        } else if (iconIdRes != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = iconIdRes),
                contentDescription = "Profile Image"
            )
        }
    }
}

@Preview
@Composable
private fun CircularProfileImagePrev() {
    HarmonyTheme {
        Column {
            CircularProfileImage()
        }
    }
}