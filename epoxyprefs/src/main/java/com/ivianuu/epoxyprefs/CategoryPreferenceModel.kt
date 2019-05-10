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

fun EpoxyController.CategoryPreference(
    body: CategoryPreferenceModel.Builder.() -> Unit
): CategoryPreferenceModel = CategoryPreferenceModel.Builder()
    .apply(body)
    .build()
    .also { it.addTo(this) }

/**
 * A category Preference
 */
open class CategoryPreferenceModel(builder: Builder) : AbstractPreferenceModel<Nothing>(builder) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CategoryPreferenceModel) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    open class Builder : AbstractPreferenceModel.Builder<Nothing>() {
        init {
            layoutRes(R.layout.item_preference_category)
        }

        override fun build() = CategoryPreferenceModel(this)
    }
}