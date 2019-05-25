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
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.airbnb.epoxy.EpoxyController

fun EpoxyController.MultiSelectListPreference(
    body: MultiSelectListPreferenceModel.Builder.() -> Unit
): MultiSelectListPreferenceModel = MultiSelectListPreferenceModel.Builder()
    .injectContextIfPossible(this)
    .apply(body)
    .build()
    .also { it.addTo(this) }

/**
 * A multi select list Preference
 */
open class MultiSelectListPreferenceModel(builder: Builder) :
    ListPreferenceModel<Set<String>>(builder) {

    override fun showDialog(context: Context) {
        var entries = entries
        if (entries == null && entriesRes != 0) {
            entries = context.resources.getStringArray(entriesRes)
        }
        if (entries == null) {
            entries = emptyArray()
        }

        var entryValues = entryValues
        if (entryValues == null && entryValuesRes != 0) {
            entryValues = context.resources.getStringArray(entryValuesRes)
        }
        if (entryValues == null) {
            entryValues = emptyArray()
        }

        val currentValues = value as? MutableSet<String> ?: mutableSetOf()
        val selectedIndices = currentValues
            .map { entryValues.indexOf(it) }
            .filter { it != -1 }
            .toIntArray()

        MaterialDialog(context)
            .applyDialogSettings()
            .listItemsMultiChoice(
                items = entries.toList(),
                initialSelection = selectedIndices,
                allowEmptySelection = true
            ) { _, positions, _ ->
                val newValue = entryValues.toList()
                    .filterIndexed { index, _ -> positions.contains(index) }
                    .map { it }
                    .toMutableSet()

                persistValue(newValue)
            }
            .show()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MultiSelectListPreferenceModel) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    open class Builder : ListPreferenceModel.Builder<Set<String>>() {
        override fun build() = MultiSelectListPreferenceModel(this)
    }
}