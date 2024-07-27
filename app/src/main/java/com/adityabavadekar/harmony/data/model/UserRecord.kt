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

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adityabavadekar.harmony.data.AchievementTypes
import com.adityabavadekar.harmony.ui.common.Gender
import com.adityabavadekar.harmony.utils.ImageAvatar
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * Represents User related all information
 */
@Entity(tableName = "accounts_table")
data class UserRecord(
    @PrimaryKey
    val uid: String,
    val created: Long,
    val birthDate: Long?,
    val gender: Gender?,
    val email: String,
    val emailVerified: Boolean,
    override val firstName: String,
    override val lastName: String?,
    val avatar: ImageAvatar.AvatarType?,
    val userFitnessRecord: UserFitnessRecord?,
    val achievements: List<AchievementTypes> = listOf(),
) : NameRecord {

    fun toThirdDegreeUserRecord(): ThirdDegreeUserRecord {
        return ThirdDegreeUserRecord(
            uid = uid,
            firstName = firstName,
            lastName = lastName,
            email = email,
            avatar = avatar
        )
    }

    companion object {
        fun fromGoogleAccount(account: GoogleSignInAccount): UserRecord {
            return UserRecord(
                uid = account.id!!,
                created = System.currentTimeMillis(),
                birthDate = null,
                gender = null,
                email = account.email!!,
                emailVerified = false,
                firstName = account.givenName ?: account.displayName ?: account.familyName
                ?: "<NOT-SET>",
                lastName = account.familyName,
                avatar = null,
                userFitnessRecord = null,
                achievements = listOf()
            )
        }
    }
}