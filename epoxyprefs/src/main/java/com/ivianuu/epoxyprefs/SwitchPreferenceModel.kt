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
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.widget_preference_switch.*

/**
 * A switch preference
 */
@EpoxyModelClass
abstract class SwitchPreferenceModel(context: Context) :
    CompoundButtonPreferenceModel(context) {

    override val Holder.compoundButton: CompoundButton?
        get() = switchWidget

    init {
        widgetLayoutRes = R.layout.widget_preference_switch
    }
}

open class SwitchPreferenceModelBuilder_(override val model: SwitchPreferenceModel) :
    CompoundButtonPreferenceModelBuilder_(model)

fun EpoxyController.switchPreference(
    context: Context,
    init: SwitchPreferenceModelBuilder_.() -> Unit
) {
    val model = SwitchPreferenceModel_(context)
    init.invoke(SwitchPreferenceModelBuilder_(model))
    model.addTo(this)
}