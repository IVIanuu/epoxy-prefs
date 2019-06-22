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
import android.preference.PreferenceManager

object EpoxyPrefsPlugins {

    private var defaultContext: PreferenceContext? = null

    fun getDefaultContext(context: Context): PreferenceContext {
        return defaultContext ?: PreferenceContext(
            context,
            PreferenceManager.getDefaultSharedPreferences(context),
            false
        ).also { defaultContext = it }
    }

    fun setDefaultContext(context: PreferenceContext) {
        this.defaultContext = context
    }

}