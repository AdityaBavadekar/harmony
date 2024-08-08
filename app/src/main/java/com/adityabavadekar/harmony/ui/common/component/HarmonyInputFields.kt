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

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HarmonyTextInput(
    hint: String,
    modifier: Modifier = Modifier,
    value: TextFieldValue = TextFieldValue(),
    onValueChanged: (TextFieldValue) -> Unit = {},
    minWidth: Boolean = false,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    trailingIcon: @Composable() (() -> Unit)? = null,
    clickable: (() -> Unit)? = null,
    editable: Boolean = true,
    enabled: Boolean = true,
    passwordInput: Boolean = false,
) {
    HarmonyTextField(
        disableMinWidth = minWidth,
        modifier = modifier
            .width(if (minWidth) IntrinsicSize.Min else IntrinsicSize.Max),
        value = value,
        onValueChange = onValueChanged,
        placeholder = {
            if (value.text.isEmpty()) Text(text = hint)
        },
        label = {
            Text(text = hint)
        },
        clickable = clickable,
        colors = colors,
        readOnly = !editable,
        visualTransformation = if (passwordInput) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (trailingIcon == null) {
                if (passwordInput) {
                    Icon(
                        painter = painterResource(id = R.drawable.visibility_on),
                        contentDescription = "Show"
                    )
                }
            } else {
                trailingIcon()
            }
        },
        enabled = enabled,
        keyboardOptions = keyboardOptions ?: KeyboardOptions(
            keyboardType = if (passwordInput) KeyboardType.Password else KeyboardType.Text
        ),
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
    )
}

@Composable
fun HarmonyListItemInputField(
    hint: String,
    itemCount: Int,
    getLabel: (Int) -> String,
    modifier: Modifier = Modifier,
    minWidth: Boolean = false,
    defaultSelectedIndex: Int = 0,
    onSelected: (Int) -> Unit = {},
) {
    val textFieldValue = remember {
        mutableStateOf(TextFieldValue(getLabel(defaultSelectedIndex)))
    }
    val isExpanded = remember {
        mutableStateOf(false)
    }

    HarmonyClickToLaunchInputField(
        hint = hint,
        value = textFieldValue.value,
        minWidth = minWidth,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.arrow_drop_down),
                contentDescription = "Show options"
            )
        },
        onClick = {
            isExpanded.value = true
        }
    )
    HarmonyPopupListPicker(
        modifier = Modifier,
        itemCount = itemCount,
        getLabel = getLabel,
        isExpanded = isExpanded.value,
        setNotExpanded = {
            isExpanded.value = false
        },
        onSelected = {
            textFieldValue.value = TextFieldValue(getLabel(it))
            onSelected(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonyTimeInputField(
    hint: String,
    modifier: Modifier = Modifier,
    onValueChanged: (Long) -> Unit = { },
    initialValue: Long? = null,
) {
    val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
    val isExpanded = remember { mutableStateOf(false) }

    val currentTime = Calendar.getInstance()

    initialValue?.let { currentTime.timeInMillis = initialValue }

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    when {
        isExpanded.value -> {
            TimePickerDialog(
                timePickerState = timePickerState,
                onDismissRequest = { hour: Int?, minute: Int? ->
                    if (hour != null && minute != null) {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        textFieldValue.value = TextFieldValue(simpleDateFormat.format(cal.time))
                        onValueChanged(cal.timeInMillis)
                    }
                    isExpanded.value = false
                }
            )
        }
    }

    HarmonyClickToLaunchInputField(
        modifier = modifier,
        hint = hint,
        value = textFieldValue.value,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.arrow_drop_down),
                contentDescription = "Show options"
            )
        },
        onClick = {
            isExpanded.value = true
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
class SelectableDatesTillToday(private val yearRange: IntRange? = null) : SelectableDates {
    private val calendar: Calendar = Calendar.getInstance()
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return calendar.timeInMillis > utcTimeMillis
    }

    override fun isSelectableYear(year: Int): Boolean {
        return yearRange?.contains(year) ?: true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAgeDatePickerState(): DatePickerState {
    val calendar = Calendar.getInstance()
    val yearRange = IntRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR))
    return rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        initialDisplayedMonthMillis = calendar.timeInMillis,
        yearRange = yearRange,
        selectableDates = SelectableDatesTillToday(yearRange = yearRange)
    )
}

@Composable
fun HarmonyClickToLaunchInputField(
    hint: String,
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit = {},
    minWidth: Boolean = false,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    passwordInput: Boolean = false,
    onClick: () -> Unit,
) {
    HarmonyTextInput(
        hint = hint,
        modifier = modifier,
        value = value,
        onValueChanged = onValueChanged,
        clickable = { onClick() },
        editable = false,
        enabled = false,
        minWidth = minWidth,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon,
        passwordInput = passwordInput,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            //For Icons
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonyDateInputField(
    hint: String,
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier,
    onValueChanged: (timeInMillis: Long) -> Unit = { },
) {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
    val isExpanded = remember { mutableStateOf(false) }

    when {
        isExpanded.value -> {
            DatePickerDialog(
                datePickerState = datePickerState,
                onDismissRequest = { timeInMillis ->
                    if (timeInMillis != null) {
                        textFieldValue.value = TextFieldValue(simpleDateFormat.format(timeInMillis))
                        onValueChanged(timeInMillis)
                    }
                    isExpanded.value = false
                }
            )
        }
    }

    HarmonyClickToLaunchInputField(
        modifier = modifier,
        hint = hint,
        value = textFieldValue.value,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.arrow_drop_down),
                contentDescription = "Show options"
            )
        },
        onClick = {
            isExpanded.value = true
        }
    )
}

@Composable
fun HarmonyPhysicalMeasurementInput(
    modifier: Modifier = Modifier,
    hint: String,
    physicalValue: Double?,
    onPhysicalValueChanged: (TextFieldValue) -> Unit = {},
    onValueChanged: (value: Double) -> Unit = {},
    unitValuesCount: Int,
    defaultUnitIndex: Int,
    maxCount: Int = 600,
    getUnitLabel: (Int) -> String,
    onUnitSelected: (Int) -> Unit,
) {
    var inputDialogIsVisible by remember { mutableStateOf(false) }
    when {
        inputDialogIsVisible -> {
            HarmonyPhysicalMeasurementInputDialog(
                maxCount = maxCount,
                physicalValue = physicalValue ?: (maxCount / 2).toDouble(),
                onDismissRequest = {
                    inputDialogIsVisible = false
                    onPhysicalValueChanged(TextFieldValue(it.toString()))
                    onValueChanged(it)
                }
            )
        }
    }

    Row(modifier.fillMaxWidth()) {
        HarmonyClickToLaunchInputField(
            hint = hint,
            value = TextFieldValue(physicalValue?.toString() ?: ""),
            modifier = Modifier.weight(1f),
            minWidth = true
        ) {
            inputDialogIsVisible = true
        }

        HarmonyListItemInputField(
            modifier = Modifier
                .width(120.dp)
                .weight(1f)
                .padding(start = 2.dp),
            minWidth = true,
            hint = "Unit",
            itemCount = unitValuesCount,
            defaultSelectedIndex = defaultUnitIndex,
            getLabel = getUnitLabel,
            onSelected = onUnitSelected,
        )
    }
}

@Composable
fun HarmonyPhysicalMeasurementInputDialog(
    modifier: Modifier = Modifier,
    maxCount: Int = 600,
    physicalValue: Double = 10.2,
    onDismissRequest: (value: Double) -> Unit = {},
) {
    val initialIntegerValue = physicalValue.toInt()
    val initialDecimalValue = (physicalValue - initialIntegerValue).times(10).toInt().toString()
    val numberPickerState = rememberPickerState()
    val decimalPickerState = rememberPickerState()

    numberPickerState.selectedItem = initialIntegerValue.toString()
    decimalPickerState.selectedItem = initialDecimalValue

    PickerDialogBase(
        onDismissRequest = { cancelled: Boolean ->
            if (cancelled) onDismissRequest(physicalValue)
            else onDismissRequest(numberPickerState.selectedItem.toInt() + 0.1 * decimalPickerState.selectedItem.toInt())
        }
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Picker(
                modifier = Modifier.weight(1f),
                itemCount = maxCount,
                getLabel = { (it + 1).toString() },
                state = numberPickerState,
                textStyle = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = ".",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.headlineLarge
            )

            Picker(
                modifier = Modifier.weight(1f),
                itemCount = 10,
                getLabel = { it.toString() },
                state = decimalPickerState,
                textStyle = MaterialTheme.typography.headlineSmall
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    timePickerState: TimePickerState,
    onDismissRequest: (hour: Int?, minute: Int?) -> Unit,
) {
    fun dismiss(useOldTime: Boolean) {
        if (useOldTime) onDismissRequest(null, null)
        else onDismissRequest(timePickerState.hour, timePickerState.minute)
    }
    PickerDialogBase(onDismissRequest = { cancelled: Boolean ->
        dismiss(useOldTime = cancelled)
    }) {
        TimePicker(state = timePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    datePickerState: DatePickerState,
    onDismissRequest: (timeInMillis: Long?) -> Unit,
) {
    fun dismiss(useOldDate: Boolean) {
        if (useOldDate) onDismissRequest(null)
        else onDismissRequest(datePickerState.selectedDateMillis)
    }

    PickerDialogBase(onDismissRequest = { cancelled: Boolean ->
        dismiss(useOldDate = cancelled)
    }) {
        DatePicker(state = datePickerState)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerDialogBase(
    onDismissRequest: (cancelled: Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest(true) },
        confirmButton = {
            Button(onClick = {
                onDismissRequest(false)
            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissRequest(true)
            }) {
                Text(text = "Cancel")
            }
        },
        text = {
            content()
        }
    )
}