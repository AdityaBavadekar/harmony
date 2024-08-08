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

import com.adityabavadekar.harmony.utils.ImageAvatar

data class ThirdDegreeUserRecord constructor(
    val uid: String,
    override val firstName: String,
    override val lastName: String?,
    val email: String,
    val avatar: ImageAvatar.AvatarType?,
) : NameRecord {
    constructor() : this(uid = "", firstName = "", lastName = "", email = "", avatar = null)

    class Builder constructor(private val uid: String) {
        private var _firstName: String? = null
        private var _lastName: String? = null
        private var _email: String? = null
        private var _avatar: ImageAvatar.AvatarType? = null

        fun setName(firstName: String, lastName: String? = null): Builder {
            _firstName = firstName
            _lastName = lastName
            return this
        }

        fun setName(name: String): Builder {
            val segments = name.split(' ')
            _firstName = segments.first()
            if (segments.size > 1) _lastName = segments.drop(1).joinToString(" ")
            return this
        }

        fun setEmail(email: String): Builder {
            _email = email
            return this
        }

        fun setAvatar(avatar: ImageAvatar.AvatarType): Builder {
            _avatar = avatar
            return this
        }

        fun build(): ThirdDegreeUserRecord {
            return ThirdDegreeUserRecord(
                uid,
                requireNotNull(_firstName),
                _lastName,
                requireNotNull(_email),
                _avatar
            )
        }
    }
}