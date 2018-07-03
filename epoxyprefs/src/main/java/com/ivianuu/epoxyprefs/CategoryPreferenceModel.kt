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
import com.airbnb.epoxy.EpoxyModelClass

/**
 * A category preference
 */
@EpoxyModelClass
abstract class CategoryPreferenceModel(context: Context) : PreferenceModel(context) {
    init {
        layoutRes = R.layout.item_preference_category
    }
}

open class CategoryPreferenceModelBuilder_(override val model: CategoryPreferenceModel) :
    PreferenceModelBuilder_(model)

fun EpoxyController.categoryPreference(
    context: Context,
    init: CategoryPreferenceModelBuilder_.() -> Unit
) {
    val model = CategoryPreferenceModel_(context)
    init.invoke(CategoryPreferenceModelBuilder_(model))
    model.addTo(this)
}