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

import androidx.compose.animation.VectorConverter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    disableMinWidth: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    clickable: (() -> Unit)? = null,
    focusRequester: FocusRequester = remember { FocusRequester() },
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    // If color is not provided via the text style, use content color as a default
    val textColor =
        textStyle.color.takeOrElse {
            val focused = interactionSource.collectIsFocusedAsState().value
            colors.textColor(enabled, isError, focused)
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    val density = LocalDensity.current
    val mEnabled = if (clickable == null) enabled else true

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier =
            modifier
                .then(
                    if (label != null) {
                        Modifier
                            // Merge semantics at the beginning of the modifier chain to ensure
                            // padding is considered part of the text field.
                            .semantics(mergeDescendants = true) {}
                            .padding(top = with(density) { getOutlinedTextFieldTopPadding().toDp() })
                    } else {
                        Modifier
                    }
                )
                .defaultMinSize(
                    minWidth = if (!disableMinWidth) OutlinedTextFieldDefaults.MinWidth else 0.dp,
                    minHeight = if (!disableMinWidth) OutlinedTextFieldDefaults.MinHeight else 0.dp
                )
                .focusRequester(focusRequester),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError)),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox =
            @Composable { innerTextField ->

                OutlinedTextFieldDefaults.DecorationBox(
                    value = value.text,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    singleLine = singleLine,
                    enabled = mEnabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    container = {
                        Container(
                            enabled = mEnabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = shape,
                            clickable = clickable,
                            focusRequester = focusRequester
                        )
                    }
                )
            }
        )
    }
}

fun TextFieldColors.textColor(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
): Color =
    when {
        !enabled -> disabledTextColor
        isError -> errorTextColor
        focused -> focusedTextColor
        else -> unfocusedTextColor
    }

@Composable
fun getOutlinedTextFieldTopPadding() = MaterialTheme.typography.bodySmall.lineHeight / 2

fun TextFieldColors.cursorColor(isError: Boolean): Color =
    if (isError) errorCursorColor else cursorColor

@Composable
fun Container(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    shape: Shape = OutlinedTextFieldDefaults.shape,
    focusedBorderThickness: Dp = OutlinedTextFieldDefaults.FocusedBorderThickness,
    unfocusedBorderThickness: Dp = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
    focusRequester: FocusRequester = remember { FocusRequester() },
    clickable: (() -> Unit)? = null,
) {
    val focused = interactionSource.collectIsFocusedAsState().value
    val borderStroke =
        animateBorderStrokeAsState(
            enabled,
            isError,
            focused,
            colors,
            focusedBorderThickness,
            unfocusedBorderThickness,
        )
    val containerColor =
        animateColorAsState(
            targetValue = colors.containerColor(enabled, isError, focused),
            animationSpec = tween(durationMillis = TextFieldAnimationDuration),
        )
    Box(
        modifier
            .border(borderStroke.value, shape)
            .then(
                if (clickable != null) {
                    Modifier
                        .clip(shape)
                        .clickableRipple(
                            onClick = {
                                focusRequester.captureFocus()
                                clickable()
                                focusRequester.freeFocus()
                            }
                        )
                } else Modifier
            )
            .textFieldBackground(containerColor::value, shape)
    )
}

@Composable
internal fun animateBorderStrokeAsState(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
    colors: TextFieldColors,
    focusedBorderThickness: Dp,
    unfocusedBorderThickness: Dp
): State<BorderStroke> {
    val targetColor = colors.indicatorColor(enabled, isError, focused)
    val indicatorColor =
        if (enabled) {
            animateColorAsState(
                targetColor,
                tween(durationMillis = TextFieldAnimationDuration)
            )
        } else {
            rememberUpdatedState(targetColor)
        }

    val thickness =
        if (enabled) {
            val targetThickness = if (focused) focusedBorderThickness else unfocusedBorderThickness
            animateDpAsState(
                targetThickness,
                tween(durationMillis = TextFieldAnimationDuration)
            )
        } else {
            rememberUpdatedState(unfocusedBorderThickness)
        }

    return rememberUpdatedState(BorderStroke(thickness.value, indicatorColor.value))
}

@Composable
fun animateDpAsState(
    targetValue: Dp,
    animationSpec: AnimationSpec<Dp> = spring<Dp>(visibilityThreshold = Dp.VisibilityThreshold),
    label: String = "DpAnimation",
    finishedListener: ((Dp) -> Unit)? = null
): State<Dp> {
    return animateValueAsState(
        targetValue,
        Dp.VectorConverter,
        animationSpec,
        label = label,
        finishedListener = finishedListener
    )
}

internal fun TextFieldColors.indicatorColor(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
): Color =
    when {
        !enabled -> disabledIndicatorColor
        isError -> errorIndicatorColor
        focused -> focusedIndicatorColor
        else -> unfocusedIndicatorColor
    }

@Composable
fun animateColorAsState(
    targetValue: Color,
    animationSpec: AnimationSpec<Color> = spring<Color>(),
    label: String = "ColorAnimation",
    finishedListener: ((Color) -> Unit)? = null
): State<Color> {
    val converter = remember(targetValue.colorSpace) {
        (Color.VectorConverter)(targetValue.colorSpace)
    }
    return animateValueAsState(
        targetValue, converter, animationSpec, label = label, finishedListener = finishedListener
    )
}


@Stable
internal fun TextFieldColors.containerColor(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
): Color =
    when {
        !enabled -> disabledContainerColor
        isError -> errorContainerColor
        focused -> focusedContainerColor
        else -> unfocusedContainerColor
    }


internal fun Modifier.textFieldBackground(
    color: ColorProducer,
    shape: Shape,
): Modifier =
    this.drawWithCache {
        val outline = shape.createOutline(size, layoutDirection, this)
        onDrawBehind { drawOutline(outline, color = color()) }
    }


const val TextFieldAnimationDuration = 150

@Preview
@Composable
private fun HTPrev() {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue("Hello text"))
    }
    HarmonyTheme {
        Surface {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                HarmonyTextField(value = textFieldValue, onValueChange = {})
                VerticalSpacer(size = 18.dp)
                HarmonyTextField(
                    value = textFieldValue,
                    onValueChange = {},
                    label = {
                        Text(text = "Label text")
                    }
                )
                Row(Modifier.fillMaxWidth()) {
                    HarmonyTextField(
                        value = textFieldValue,
                        modifier = Modifier.weight(1f),
                        onValueChange = {},
                        label = {
                            Text(text = "Label text")
                        }
                    )
                    HarmonyTextField(
                        value = textFieldValue,
                        modifier = Modifier.weight(1f),
                        onValueChange = {},
                        label = {
                            Text(text = "Label text")
                        }
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    HarmonyTextField(
                        value = textFieldValue,
                        modifier = Modifier.weight(1f),
                        onValueChange = {},
                        label = {
                            Text(text = "Label text")
                        }
                    )
                    HarmonyTextField(
                        value = textFieldValue,
                        disableMinWidth = true,
                        onValueChange = {},
                        label = {
                            Text(text = "Label text")
                        }
                    )
                }
                VerticalSpacer(size = 18.dp)
                val defaults = OutlinedTextFieldDefaults.colors()
                HarmonyTextField(
                    clickable = {
                        if (textFieldValue.text == "Focused") {
                            textFieldValue = TextFieldValue("Hello text")
                        } else {
                            textFieldValue = TextFieldValue("Focused")
                        }
                    },
                    value = textFieldValue,
                    onValueChange = {},
                    enabled = false,
                    readOnly = true,
                    label = {
                        Text(text = "Label text")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = defaults.unfocusedContainerColor,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.primary,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        //For Icons
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
