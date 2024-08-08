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

import com.adityabavadekar.harmony.ui.common.Gender
import com.adityabavadekar.harmony.ui.common.LengthUnits
import com.adityabavadekar.harmony.ui.common.MassUnits

data class SignInUiState(
    val email: String? = null,
    val name: String? = null,
    val gender: Gender? = Gender.MALE,
    val weight: Double? = null,
    val weightUnit: MassUnits = MassUnits.KG,
    val height: Double? = null,
    val heightUnit: LengthUnits = LengthUnits.CENTIMETERS,
    val dateOfBirth: Long? = null,
    val isLoggedIn: Boolean = false,
    val currentScreenType: SigninActivity.SigninScreenType = SigninActivity.SigninScreenType.GOOGLE_SIGNIN,
)
