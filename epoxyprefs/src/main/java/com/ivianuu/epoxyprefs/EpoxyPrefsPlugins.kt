/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.epoxyprefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Global config
 */
object EpoxyPrefsPlugins {

    private var defaultSharedPreferences: SharedPreferences? = null
    private var defaultSharedPreferencesName: String? = null
    private var useCommit = false

    fun getDefaultSharedPreferences(context: Context): SharedPreferences =
        this.defaultSharedPreferences
                ?: context.getSharedPreferences(
                    getDefaultSharedPreferencesName(context), Context.MODE_PRIVATE
                ).also {
                    defaultSharedPreferences = it
                }

    fun setDefaultSharedPreferences(sharedPreferences: SharedPreferences) {
        this.defaultSharedPreferences = sharedPreferences
    }

    fun getDefaultSharedPreferencesName(context: Context) =
        defaultSharedPreferencesName ?: context.packageName+"_preferences"

    fun setDefaultSharedPreferencesName(defaultSharedPreferencesName: String) {
        this.defaultSharedPreferencesName = defaultSharedPreferencesName
    }

    fun getUseCommit() = useCommit

    fun setUseCommit(useCommit: Boolean) {
        this.useCommit = useCommit
    }
}