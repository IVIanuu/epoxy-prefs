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
import kotlinx.android.synthetic.main.widget_preference_switch.switchWidget

/**
 * A switch preference
 */
open class SwitchPreferenceModel(builder: Builder) : CompoundButtonPreferenceModel(builder) {

    override val Holder.compoundButton: CompoundButton?
        get() = switchWidget

    open class Builder(context: Context) : CompoundButtonPreferenceModel.Builder(context) {

        init {
            widgetLayoutRes(R.layout.widget_preference_switch)
        }

        override fun build(): SwitchPreferenceModel = SwitchPreferenceModel(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SwitchPreferenceModel) return false
        if (!super.equals(other)) return false
        return true
    }

}

fun EpoxyController.switchPreference(
    context: Context,
    init: SwitchPreferenceModel.Builder.() -> Unit
): SwitchPreferenceModel {
    return SwitchPreferenceModel.Builder(context)
        .apply(init)
        .build()
        .also { it.addTo(this) }
}

fun PreferenceEpoxyController.switchPreference(
    init: SwitchPreferenceModel.Builder.() -> Unit
): SwitchPreferenceModel = switchPreference(context, init)