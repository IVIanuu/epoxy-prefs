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

import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyController
import kotlinx.android.synthetic.main.widget_preference_switch.*

fun EpoxyController.SwitchPreference(
    body: SwitchPreferenceModel.Builder.() -> Unit
): SwitchPreferenceModel = SwitchPreferenceModel.Builder()
    .injectContextIfPossible(this)
    .apply(body)
    .build()
    .also { it.addTo(this) }

open class SwitchPreferenceModel(builder: Builder) : CompoundButtonPreferenceModel(builder) {

    override val Holder.compoundButton: CompoundButton?
        get() = switchWidget

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SwitchPreferenceModel) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    open class Builder : CompoundButtonPreferenceModel.Builder() {
        init {
            widgetLayoutRes(R.layout.widget_preference_switch)
        }

        override fun build() = SwitchPreferenceModel(this)
    }
}