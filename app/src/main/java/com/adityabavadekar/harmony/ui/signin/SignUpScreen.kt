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

package com.adityabavadekar.harmony.ui.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.common.Gender
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.MassUnits
import com.adityabavadekar.harmony.ui.common.component.CircularProfileImage
import com.adityabavadekar.harmony.ui.common.component.CircularProfileImageSize
import com.adityabavadekar.harmony.ui.common.component.HarmonyDatePicker
import com.adityabavadekar.harmony.ui.common.component.HarmonyListItemInputField
import com.adityabavadekar.harmony.ui.common.component.HarmonyPhysicalMeasurementInput
import com.adityabavadekar.harmony.ui.common.component.HarmonyTextInput
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.common.component.rememberAgeDatePickerState
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import java.util.Calendar

@Composable
fun DecorationBox(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.padding(
                horizontal = 18.dp,
                vertical = 16.dp
            )
        ) {
            content()
        }
    }
}

@Composable
fun BigTitleAndButtonsScreen(
    bigTitleText: String,
    positiveButtonText: String = "Next",
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    innerContent: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = bigTitleText,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(28.dp)
            )

            Column(
                Modifier
                    .padding(vertical = 28.dp)
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                innerContent()
            }
            Column {
                BottomBar(
                    positiveButtonText = positiveButtonText,
                    onPositiveClick = onNext,
                    onNegativeClick = onPrevious
                )
            }
        }
    }

}

@Composable
fun SignUpEmailInputScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    val email = remember {
        mutableStateOf(TextFieldValue())
    }
    BigTitleAndButtonsScreen(
        bigTitleText = "Sign Up",
        onNext = onNext,
        onPrevious = onPrevious
    ) {
        DecorationBox {
            HarmonyTextInput(
                hint = "Email address",
                value = email.value,
                onValueChanged = { email.value = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpBirthdateInputScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val calculatedAge = remember {
        mutableIntStateOf(0)
    }
    val datePickerState = rememberAgeDatePickerState()


    BigTitleAndButtonsScreen(
        bigTitleText = "What is your date of birth?",
        onNext = onNext,
        onPrevious = onPrevious
    ) {

        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val c = Calendar.getInstance()
            datePickerState.selectedDateMillis?.let {
                c.timeInMillis = it
                calculatedAge.intValue = currentYear - c.get(Calendar.YEAR)
            }
            Text(
                text = "You are ${calculatedAge.intValue} years of age",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Light),
                modifier = Modifier.padding(
                    horizontal = 28.dp
                )
            )
            VerticalSpacer()
            DatePicker(
                modifier = Modifier.padding(18.dp),
                state = datePickerState,
                colors = DatePickerDefaults.colors().copy(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                title = {}
            )
        }
    }
}

@Composable
fun SignUpNamePasswordInputScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    val name = remember {
        mutableStateOf(TextFieldValue())
    }
    val password = remember {
        mutableStateOf(TextFieldValue())
    }


    BigTitleAndButtonsScreen(
        bigTitleText = "Name and Password",
        positiveButtonText = "Create my account",
        onNext = onNext,
        onPrevious = onPrevious
    ) {
        Column {
            CircularProfileImage(
                size = CircularProfileImageSize.MEDIUM,
                margin = PaddingValues(8.dp)
            )
        }
        VerticalSpacer()

        DecorationBox {
            Column {
                HarmonyTextInput(
                    hint = "Full name",
                    value = name.value,
                    onValueChanged = { name.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                Spacer(modifier = Modifier.height(8.dp))
                HarmonyTextInput(
                    hint = "Password",
                    value = password.value,
                    onValueChanged = { password.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
            }
        }
    }
}

@Composable
fun SignUpAfterScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    var gender = Gender.MALE
    var heightUnit = LengthUnits.CENTIMETERS
    var weightUnit = MassUnits.KG

    val height = remember {
        mutableStateOf(TextFieldValue())
    }
    val weight = remember {
        mutableStateOf(TextFieldValue())
    }

    BigTitleAndButtonsScreen(
        bigTitleText = "About you",
        positiveButtonText = "Save",
        onNext = onNext,
        onPrevious = onPrevious
    ) {
        Column(
            Modifier
                .padding(vertical = 28.dp, horizontal = 18.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProfileImage(
                    size = CircularProfileImageSize.MEDIUM,
                    margin = PaddingValues(8.dp)
                )
                Text(
                    modifier = Modifier.alpha(0.8f),
                    text = "User Name",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            DecorationBox {
                Column(Modifier.padding(horizontal = 8.dp)) {
                    HarmonyListItemInputField(
                        modifier = Modifier.fillMaxWidth(),
                        hint = "Gender",
                        itemCount = Gender.entries.size,
                        defaultSelectedIndex = gender.ordinal,
                        getLabel = {
                            Gender.entries[it].name.lowercase()
                                .replaceFirstChar { c -> c.uppercaseChar() }
                        },
                        onSelected = { gender = Gender.entries[it] }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HarmonyPhysicalMeasurementInput(
                        hint = "Weight",
                        physicalValue = weight.value,
                        onPhysicalValueChanged = { weight.value = it },
                        unitValuesCount = MassUnits.entries.size,
                        defaultUnitIndex = weightUnit.ordinal,
                        getUnitLabel = { MassUnits.entries[it].shortSymbol() },
                        onUnitSelected = { weightUnit = MassUnits.entries[it] }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HarmonyPhysicalMeasurementInput(
                        hint = "Height",
                        physicalValue = height.value,
                        onPhysicalValueChanged = { height.value = it },
                        unitValuesCount = LengthUnits.entries.size,
                        defaultUnitIndex = heightUnit.ordinal,
                        getUnitLabel = { LengthUnits.entries[it].shortSymbol() },
                        onUnitSelected = { heightUnit = LengthUnits.entries[it] }
                    )
                }
            }

        }
    }
}

enum class SignUpScreenType { NONE, EMAIL, DOB, NAME_PASSWORD, PROFILE }

fun hasNextSignUpScreen(signUpScreenType: SignUpScreenType): Boolean {
    return signUpScreenType != SignUpScreenType.NAME_PASSWORD
}

@Composable
fun GetNextSignUpScreen(
    signUpScreenType: SignUpScreenType,
    onNext: (currentType: SignUpScreenType) -> Unit,
    onPrevious: (currentType: SignUpScreenType) -> Unit,
) {
    return when (signUpScreenType) {
        SignUpScreenType.NONE -> SignUpEmailInputScreen(
            onNext = { onNext(SignUpScreenType.EMAIL) },
            onPrevious = { onPrevious(SignUpScreenType.EMAIL) })

        SignUpScreenType.EMAIL -> SignUpBirthdateInputScreen(
            onNext = { onNext(SignUpScreenType.DOB) },
            onPrevious = { onPrevious(SignUpScreenType.DOB) })

        SignUpScreenType.DOB -> SignUpNamePasswordInputScreen(
            onNext = { onNext(SignUpScreenType.NAME_PASSWORD) },
            onPrevious = { onPrevious(SignUpScreenType.NAME_PASSWORD) })

        SignUpScreenType.NAME_PASSWORD -> SignUpAfterScreen(
            onNext = { onNext(SignUpScreenType.PROFILE) },
            onPrevious = { onPrevious(SignUpScreenType.PROFILE) })

        else -> {
            if (!hasNextSignUpScreen(signUpScreenType)) throw IllegalStateException("Next Screen requested while there was none")
            else throw UnknownError()
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPrevA() {
    HarmonyTheme {
        Surface {
            SignUpEmailInputScreen()
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPrevB() {
    HarmonyTheme {
        Surface {
            SignUpBirthdateInputScreen()
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPrevC() {
    HarmonyTheme {
        Surface {
            SignUpNamePasswordInputScreen()
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPrevD() {
    HarmonyTheme {
        Surface {
            SignUpAfterScreen()
        }
    }
}
