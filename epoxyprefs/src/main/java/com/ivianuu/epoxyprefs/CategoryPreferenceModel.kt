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
import com.airbnb.epoxy.EpoxyController

/**
 * A category preference
 */
open class CategoryPreferenceModel(builder: Builder) : PreferenceModel(builder) {
    open class Builder(context: Context) : PreferenceModel.Builder(context) {

        init {
            layoutRes(R.layout.item_preference_category)
        }

        override fun build() = CategoryPreferenceModel(this)
    }
}

inline fun EpoxyController.categoryPreference(
    context: Context,
    init: CategoryPreferenceModel.Builder.() -> Unit
) = CategoryPreferenceModel.Builder(context)
    .apply(init)
    .build()
    .also { it.addTo(this) }

inline fun PreferenceEpoxyController.categoryPreference(
    init: CategoryPreferenceModel.Builder.() -> Unit
) = categoryPreference(context, init)