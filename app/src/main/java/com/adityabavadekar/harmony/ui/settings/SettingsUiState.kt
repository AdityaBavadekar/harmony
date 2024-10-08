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

package com.adityabavadekar.harmony.ui.settings

import com.adityabavadekar.harmony.data.ApplicationTheme
import com.adityabavadekar.harmony.data.model.ThirdDegreeUserRecord

data class SettingsUiState(
    val theme: ApplicationTheme = ApplicationTheme.SYSTEM_DEFAULT,
    val thirdDegreeUserRecord: ThirdDegreeUserRecord = ThirdDegreeUserRecord(),
)
