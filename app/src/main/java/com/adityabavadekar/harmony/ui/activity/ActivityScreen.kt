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

package com.adityabavadekar.harmony.ui.activity

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityScreen(modifier: Modifier = Modifier) {

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Activity",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(28.dp)
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn {
                    item {
                        ActivityItem(
                            "Cycling",
                            "12 Kcal",
                            System.currentTimeMillis()
                        )
                    }
                    item {
                        ActivityItem2(
                            "Cycling",
                            "12 Kcal",
                            System.currentTimeMillis()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(title: String, text: String, timestamp: Long) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.Yellow)
            .clickableRipple()
    ) {
        Row(
            Modifier.padding(horizontal = 28.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.alpha(0.9f),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.W400
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column {
                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.alpha(0.7f),
                        text = SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                        ).format(Date(timestamp)),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    )
                    Text(
                        modifier = Modifier
                            .alpha(0.7f)
                            .padding(horizontal = 2.dp),
                        text = "•",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        modifier = Modifier.alpha(0.7f),
                        text = "1:30s",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    ) {
                        Box(
                            Modifier.padding(4.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "50", style = MaterialTheme.typography.bodySmall)
                                HorizontalSpacer(size = 4.dp)
                                Icon(
                                    painter = painterResource(id = R.drawable.steps),
                                    contentDescription = "Steps count",
                                    modifier = Modifier.size(MaterialTheme.typography.bodySmall.fontSize.value.dp)
                                )
                            }
                        }
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        )
                    ) {
                        Box(
                            Modifier.padding(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "8", style = MaterialTheme.typography.bodySmall)
                                HorizontalSpacer(size = 4.dp)
                                Text(text = "km", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem2(title: String, text: String, timestamp: Long) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickableRipple()
    ) {
        Row(
            Modifier.padding(horizontal = 28.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.alpha(0.9f),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.W400
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.alpha(0.7f),
                        text = SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                        ).format(Date(timestamp)),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    )
                    Text(
                        modifier = Modifier
                            .alpha(0.7f)
                            .padding(horizontal = 2.dp),
                        text = "•",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        modifier = Modifier.alpha(0.7f),
                        text = "1:30s",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val colorsList = listOf(
                    Color(0xFFD8EFD3),
                    Color(0xFFFEFFD2),
                    Color(0xFFE7D4B5),
                )
                Row {
                    NutItem(
                        text = "12 kcal",
                        color = colorsList[0],
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
                    NutItem(
                        text = "50",
                        color = colorsList[1],
                        trailingIcon = R.drawable.steps,
                        trailingIconContentDescription = "Steps count",
                        shape = RoundedCornerShape(0.dp)
                    )
                    NutItem(
                        text = "8 km",
                        color = colorsList[2],
                        shape = RoundedCornerShape(
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NutItem(
    text: String,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    @DrawableRes trailingIcon: Int? = null,
    trailingIconContentDescription: String? = null,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor)
    Surface(
        color = color,
        shape = shape
    ) {
        Box(Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = text, style = textStyle)
                HorizontalSpacer(size = 4.dp)
                trailingIcon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = trailingIconContentDescription,
                        modifier = Modifier.size(textStyle.fontSize.value.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ActivityScreenPrev() {
    HarmonyTheme {
        Surface {
            ActivityScreen()
        }
    }
}