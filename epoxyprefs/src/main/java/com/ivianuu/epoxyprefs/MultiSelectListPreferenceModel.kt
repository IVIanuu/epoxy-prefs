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
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModelClass

/**
 * A multi select list preference
 */
@EpoxyModelClass
abstract class MultiSelectListPreferenceModel(
    context: Context
) : ListPreferenceModel(context) {

    override fun showDialog() {
        val key = key ?: return
        val entries = entries ?: return
        val entryValues = entryValues ?: return

        val currentValues = getPersistedStringSet(key)
        val selectedIndices = currentValues
            .map { entryValues.indexOf(it) }
            .filter { it != -1 }
            .toTypedArray()

        MaterialDialog.Builder(context)
            .applyDialogSettings()
            .items(entries.toList())
            .itemsCallbackMultiChoice(selectedIndices) { _: MaterialDialog, positions: Array<Int>, _: Array<CharSequence> ->
                val newValue = entryValues.toList()
                    .filterIndexed { index, _ -> positions.contains(index) }
                    .map(CharSequence::toString)
                    .toMutableSet()

                if (callChangeListener(newValue)) {
                    persistStringSet(key, newValue)
                    true
                } else {
                    false
                }
            }
            .show()
    }
}

fun EpoxyController.multiSelectListPreference(
    context: Context,
    init: MultiSelectListPreferenceModel.() -> Unit
) {
    val model = MultiSelectListPreferenceModel_(context)
    init.invoke(model)
    model.addTo(this)
}