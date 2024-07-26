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

package com.adityabavadekar.harmony.data.model

import com.adityabavadekar.harmony.data.AchievementTypes
import com.adityabavadekar.harmony.ui.common.Gender
import com.adityabavadekar.harmony.utils.ImageAvatar

/**
 * Represents User related all information
 */
data class UserRecord(
    override val firstName: String,
    override val lastName: String?,
    val uid: String,
    val created: Long,
    val birthDate: Long?,
    val gender: Gender,
    val email: String,
    val emailVerified: Boolean,
    val avatar: ImageAvatar.AvatarType?,
    val userFitnessRecord: UserFitnessRecord,
    val achievements: List<AchievementTypes>,
) : NameRecord