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

package com.ivianuu.epoxyprefs.util

import android.content.Intent
import com.ivianuu.epoxyprefs.PreferenceModel

/**
 * A click listener to launch intents on preference clicks
 */
open class IntentPreferenceModelClickListener : PreferenceModel.ClickListener {

    private val intent: Intent?
    private val intentProvider: ((PreferenceModel) -> Intent?)?

    constructor(intent: Intent) {
        this.intent = intent
        this.intentProvider = null
    }

    constructor(intent: (preference: PreferenceModel) -> Intent?) {
        this.intent = null
        this.intentProvider = intent
    }

    override fun onPreferenceClicked(preference: PreferenceModel): Boolean {
        val intent = intent ?: intentProvider?.invoke(preference) ?: return false
        preference.context.startActivity(intent)
        return true
    }

}