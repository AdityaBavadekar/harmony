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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabavadekar.harmony.ui.common.component.HarmonyTextInput
import com.adityabavadekar.harmony.ui.common.component.HorizontalSpacer
import com.adityabavadekar.harmony.ui.common.component.ThemedGoogleSigninButton
import com.adityabavadekar.harmony.ui.common.component.VerticalSpacer
import com.adityabavadekar.harmony.ui.theme.HarmonyTheme
import com.adityabavadekar.harmony.ui.theme.LinkColor
import com.adityabavadekar.harmony.utils.ThemePreviews

@Composable
fun SignInScreen(
    signinWithGoogle: () -> Unit = {},
    switchToSignUp: () -> Unit = {},
    onContinue: () -> Unit = {},
) {
    val email = remember {
        mutableStateOf(TextFieldValue())
    }
    val password = remember {
        mutableStateOf(TextFieldValue())
    }
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign In",
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
                HarmonyTextInput(
                    hint = "Email address",
                    value = email.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChanged = { email.value = it })

                Spacer(modifier = Modifier.height(8.dp))

                HarmonyTextInput(
                    hint = "Password",
                    value = password.value,
                    passwordInput = true,
                    onValueChanged = { password.value = it })

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThemedGoogleSigninButton(signinWithGoogle)
                SignInScreenSwitcher(
                    normalText = "Don't have an account?",
                    clickableText = "Sign Up",
                    onClick = switchToSignUp
                )
                BottomBar(onPositiveClick = onContinue)
            }
        }
    }
}

@Composable
fun SignInBackgroudGradient(content: @Composable BoxScope.() -> Unit) {
    val alpha = 0.3f
    val lightColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primary,
        Color.DarkGray,
        Color.Blue,
    )
    val darkColors = listOf(
        MaterialTheme.colorScheme.primary,
        Color.Black,
        Color.Blue,
    )
    val colors = if (isSystemInDarkTheme()) darkColors else lightColors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors.map { it.copy(alpha = alpha) }
                )
            ),
        content = content
    )
}

@Composable
fun GoogleSignInScreen(
    signinWithGoogle: () -> Unit = {},
) {
    SignInBackgroudGradient {
        Column {

            BigTitleAndButtonsScreen(
                bigTitleText = "Sign In",
                negativeButtonText = null,
                positiveButtonText = null
            ) {
                Column(
                    Modifier
                        .padding(18.dp)
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    DecorationBox {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            VerticalSpacer()
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = "Join Harmony and start your wellness journey today.",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .alpha(0.7f),
                                text = "Sign in with Google to continue",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelLarge
                            )
                            VerticalSpacer(size = 50.dp)
                            ThemedGoogleSigninButton(
                                onClick = signinWithGoogle,
                                fillWidth = true
                            )
                            VerticalSpacer()
                        }
                    }

                    /*VerticalSpacer()
                    SignInScreenSwitcher(
                        normalText = "Don't have an account?",
                        clickableText = "Sign Up",
                        onClick = switchToSignUp
                    )*/
                }
            }
        }
    }
}

@Composable
fun SignInScreenSwitcher(
    normalText: String,
    clickableText: String,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp, horizontal = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = normalText,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.clickable { onClick() },
            text = clickableText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
            color = LinkColor
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    negativeButtonText: String? = "Back",
    positiveButtonText: String? = "Continue",
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (negativeButtonText != null) {
            OutlinedButton(onClick = onNegativeClick) {
                Text(text = negativeButtonText)
            }
        } else {
            HorizontalSpacer()
        }
        if (positiveButtonText != null) {
            Button(onClick = onPositiveClick) {
                Text(text = positiveButtonText)
            }
        } else {
            HorizontalSpacer()
        }
    }
}

@ThemePreviews
@Composable
private fun SignInScreenPrev() {
    HarmonyTheme {
        Surface {
            GoogleSignInScreen()
        }
    }
}