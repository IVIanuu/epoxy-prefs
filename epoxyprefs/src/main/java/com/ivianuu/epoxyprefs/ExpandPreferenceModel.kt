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

import com.airbnb.epoxy.EpoxyController

fun EpoxyController.ExpandPreference(
    body: ExpandPreferenceModel.Builder.() -> Unit
): ExpandPreferenceModel = ExpandPreferenceModel.Builder()
    .injectContextIfPossible(this)
    .apply(body)
    .build()
    .also { it.addTo(this) }

open class ExpandPreferenceModel(builder: Builder) : AbstractPreferenceModel<Nothing>(builder) {

    open class Builder : AbstractPreferenceModel.Builder<Nothing>() {

        init {
            widgetLayoutRes(R.layout.widget_preference_expand_button)
        }

        override fun build() = ExpandPreferenceModel(this)
    }
}