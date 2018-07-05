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
import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyController
import kotlinx.android.synthetic.main.widget_preference_checkbox.*

/**
 * A check box preference
 */
open class CheckboxPreferenceModel(builder: Builder) : CompoundButtonPreferenceModel(builder) {

    override val Holder.compoundButton: CompoundButton?
        get() = checkbox

    open class Builder(context: Context) : CompoundButtonPreferenceModel.Builder(context) {

        init {
            widgetLayoutRes(R.layout.widget_preference_checkbox)
        }

        override fun build() = CheckboxPreferenceModel(this)
    }
}

inline fun EpoxyController.checkboxPreference(
    context: Context,
    init: CheckboxPreferenceModel.Builder.() -> Unit
) = CheckboxPreferenceModel.Builder(context)
    .apply(init)
    .build()
    .also { it.addTo(this) }

inline fun PreferenceEpoxyController.checkboxPreference(
    init: CheckboxPreferenceModel.Builder.() -> Unit
) = checkboxPreference(context, init)