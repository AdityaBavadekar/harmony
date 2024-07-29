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

package com.adityabavadekar.harmony.ui.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.adityabavadekar.harmony.R
import com.adityabavadekar.harmony.data.model.UserRecord
import com.adityabavadekar.harmony.ui.common.Gender
import com.adityabavadekar.harmony.ui.common.Length
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.Mass
import com.adityabavadekar.harmony.ui.common.MassUnits
import com.adityabavadekar.harmony.ui.common.component.CircularProfileImage
import com.adityabavadekar.harmony.ui.common.component.CircularProfileImageSize
import com.adityabavadekar.harmony.ui.common.component.HarmonyPhysicalMeasurementInput
import com.adityabavadekar.harmony.ui.common.component.HarmonyTextInput
import com.adityabavadekar.harmony.ui.common.component.HarmonyTimeInputField
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.Label
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.component.clickableRipple
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.utils.ImageAvatar
import com.adityabavadekar.harmony.utils.capFirstChar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    account: UserRecord? = null,
) {
    var heightUnit = LengthUnits.CENTIMETERS
    var weightUnit = MassUnits.KG

    val height = remember {
        mutableStateOf(account?.userFitnessRecord?.height)
    }

    val weight = remember {
        mutableStateOf(account?.userFitnessRecord?.weight)
    }

    val stepsTarget = remember {
        mutableStateOf(TextFieldValue(account?.userFitnessRecord?.stepsGoal?.toString() ?: ""))
    }

    val sleepTarget = remember {
        mutableStateOf(account?.userFitnessRecord?.bedtimeSleep)
    }

    val wakeupTarget = remember {
        mutableStateOf(account?.userFitnessRecord?.bedtimeWakeUp)
    }

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                item {
                    ProfileScreenCategory(labelText = "About you") {
                        Surface(
                            modifier = Modifier
                                .padding(it),
                            color = (MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableRipple()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProfileImage(
                                    size = CircularProfileImageSize.SMALL,
                                    iconIdRes = account?.avatar?.drawableRes
                                        ?: ImageAvatar.getMaleAvatar(2),
                                    margin = PaddingValues(8.dp)
                                )
                                HorizontalSpacer()
                                Text(
                                    modifier = Modifier.alpha(0.8f),
                                    text = account?.getName() ?: "Name",
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }
                        VerticalSpacer()
                        RowItem(
                            modifier = Modifier.padding(it),
                            labelText = "Gender",
                            valueText = account?.gender?.name?.capFirstChar() ?: ""
                        )
                        val dob = account?.birthDate?.let { timeInMillis ->
                            SimpleDateFormat(
                                "dd MMMM yyyy",
                                Locale.getDefault()
                            ).format(Date(timeInMillis))
                        }
                        RowItem(
                            modifier = Modifier.padding(it),
                            labelText = "Date of Birth",
                            valueText = dob ?: ""
                        )
                    }

                }

                item {
                    ProfileScreenCategory(labelText = "Your Goals") { paddingValues ->
                        Column(Modifier.padding(paddingValues)) {

                            HarmonyTextInput(
                                modifier = Modifier.fillMaxWidth(),
                                value = stepsTarget.value,
                                onValueChanged = {
                                    if (it.text.isDigitsOnly()) stepsTarget.value = it
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                hint = "Steps Target"
                            )
                        }
                    }
                }

                item {
                    ProfileScreenCategory(labelText = "Bedtime") {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(it)
                        ) {
                            HarmonyTimeInputField(
                                modifier = Modifier.weight(1f, fill = false),
                                hint = "Sleep",
                                initialValue = sleepTarget.value,
                                onValueChanged = { v -> sleepTarget.value = v }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            HarmonyTimeInputField(
                                modifier = Modifier.weight(1f, fill = false),
                                hint = "Wake up",
                                initialValue = wakeupTarget.value,
                                onValueChanged = { v -> wakeupTarget.value = v }
                            )
                        }
                    }
                }

                item {

                    ProfileScreenCategory(labelText = "Body Measurements") { paddingValues ->
                        Column(Modifier.padding(paddingValues)) {
                            HarmonyPhysicalMeasurementInput(
                                hint = "Weight",
                                physicalValue = weight.value?.getSIValue(),
                                onValueChanged = { weight.value = Mass(it) },
                                unitValuesCount = MassUnits.entries.size,
                                defaultUnitIndex = weightUnit.ordinal,
                                getUnitLabel = { MassUnits.entries[it].shortSymbol() },
                                onUnitSelected = { weightUnit = MassUnits.entries[it] }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            HarmonyPhysicalMeasurementInput(
                                hint = "Height",
                                physicalValue = height.value?.getSIValue(),
                                onValueChanged = { height.value = Length(it) },
                                unitValuesCount = LengthUnits.entries.size,
                                defaultUnitIndex = heightUnit.ordinal,
                                getUnitLabel = { LengthUnits.entries[it].shortSymbol() },
                                onUnitSelected = { heightUnit = LengthUnits.entries[it] }
                            )
                        }
                    }
                }
                item { VerticalSpacer(size = 100.dp) }
            }
        }
    }
}

@Composable
fun ProfileScreenCategory(
    labelText: String,
    paddingValues: PaddingValues = PaddingValues(horizontal = 14.dp),
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    Column(
        Modifier
            .padding(top = 8.dp)
    ) {
        Label(
            padding = PaddingValues(start = 18.dp, bottom = 8.dp, top = 18.dp),
            large = true,
            text = labelText,
            fontWeight = FontWeight.W400
        )
        Column {
            content(
                PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    bottom = 4.dp
                )
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPrev() {
    HarmonyTheme {
        Surface {
            ProfileScreen()
        }
    }
}

@Composable
private fun RowItem(
    modifier: Modifier = Modifier,
    labelText: String,
    valueText: String,
    @DrawableRes iconRes: Int = R.drawable.baseline_navigate_next_24,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickableRipple()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .then(modifier)
                .padding(vertical = 18.dp)
                .padding(end = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier.padding(start = 8.dp),
            ) {
                Text(
                    modifier = Modifier.alpha(0.8f),
                    text = labelText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W400,
                )
                VerticalSpacer(size = 4.dp)
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = ""
                )
            }
        }
    }
}