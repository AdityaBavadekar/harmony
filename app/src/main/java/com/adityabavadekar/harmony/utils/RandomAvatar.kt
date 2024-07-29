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

package com.adityabavadekar.harmony.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.Gender
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import java.io.File
import java.io.FileOutputStream

class ImageAvatar {
    enum class AvatarType(val drawableRes: Int, val gender: Gender) {
        MALE1(R.drawable.m1, Gender.MALE),
        MALE2(R.drawable.m2, Gender.MALE),
        MALE3(R.drawable.m3, Gender.MALE),
        MALE4(R.drawable.m4, Gender.MALE),
        MALE5(
            R.drawable.m5, Gender.MALE
        ),
        MALE6(R.drawable.m6, Gender.MALE),
        MALE7(R.drawable.m7, Gender.MALE),
        FEMALE1(R.drawable.m1, Gender.FEMALE),
        FEMALE2(R.drawable.m2, Gender.FEMALE),
        FEMALE3(R.drawable.m3, Gender.FEMALE),
        FEMALE4(R.drawable.m4, Gender.FEMALE),
        FEMALE5(R.drawable.m5, Gender.FEMALE),
        FEMALE6(R.drawable.m6, Gender.FEMALE),
        FEMALE7(R.drawable.m7, Gender.FEMALE),
        NEUTRAL_SMILE(R.drawable.smilie_pixel, Gender.OTHER)
    }

    companion object {
        private const val FILE_INITIALS_AVATAR = "initials_avatar"
        private const val FILE_GENERAL_AVATAR = "general_avatar"
        private val MALE_AVATARS =
            AvatarType.entries.filter { it.gender == Gender.MALE }.map { it.drawableRes }
        private val FEMALE_AVATARS =
            AvatarType.entries.filter { it.gender == Gender.FEMALE }.map { it.drawableRes }
        private val O_AVATARS =
            AvatarType.entries.filter { it.gender == Gender.OTHER }.map { it.drawableRes }

        fun write(context: Context, fileName: String, bitmap: Bitmap): Boolean {
            val file = File(context.dataDir, "${fileName}.png")
            try {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                return true
            } catch (e: Exception) {
                return false
            }
        }

        fun load(fileName: String, context: Context): Bitmap? {
            return BitmapFactory.decodeFile(File(context.dataDir, "${fileName}.png").path)
        }

        fun getRandom(): Int {
            return AvatarType.entries.random().drawableRes
        }

        fun getMaleAvatar(id: Int = -1): Int {
            return if (id < 0) MALE_AVATARS.random() else MALE_AVATARS[id]
        }

        fun getFemaleAvatar(id: Int = -1): Int {
            return if (id < 0) FEMALE_AVATARS.random() else FEMALE_AVATARS[id]
        }

        fun getOAvatar(): Int {
            return O_AVATARS.random()
        }

    }
}

data class ColorPair(val textColor: Color, val backgroundColor: Color)

val colorPairs = listOf(
    ColorPair(textColor = Color.Black, backgroundColor = Color(0xFFD3D3D3)), // Black on Light Gray
    ColorPair(textColor = Color.White, backgroundColor = Color(0xFF2196F3)), // White on Blue
    ColorPair(textColor = Color.Black, backgroundColor = Color(0xFFFFEB3B)), // Black on Yellow
    ColorPair(textColor = Color.White, backgroundColor = Color(0xFFF44336)), // White on Red
    ColorPair(textColor = Color.Black, backgroundColor = Color(0xFF4CAF50)),  // Black on Green
    ColorPair(textColor = Color.Black, backgroundColor = Color(0xFFFF9800))  // Black on Orange
)

@Composable
fun InitialsAvatar(
    initials: String,
    size: Dp = 100.dp,
    colorPair: ColorPair = colorPairs.random(),
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(colorPair.backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.displayMedium,
            color = colorPair.textColor
        )
    }
}

@Preview
@Composable
private fun Avater() {
    HarmonyTheme {
        Column {
            val composeCaptureState = rememberComposeCaptureState()
            val initials = "AD"
            ComposeCapture(composeCaptureState) {
                InitialsAvatar(initials = initials, colorPair = colorPairs[0])
            }
        }
    }
}
