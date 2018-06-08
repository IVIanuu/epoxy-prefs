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
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.epoxy.EpoxyModelClass

/**
 * A single item preference
 */
@EpoxyModelClass
abstract class SingleItemListPreferenceModel(
    context: Context
) : ListPreferenceModel(context) {

    override fun showDialog() {
        val key = key ?: return
        val entries = entries ?: return
        val entryValues = entryValues ?: return

        val currentValue = getPersistedString(key)
        val selectedIndex = entryValues.indexOf(currentValue)

        MaterialDialog.Builder(context)
            .applyDialogSettings()
            .items(entries.toList())
            .itemsCallbackSingleChoice(selectedIndex) { _: MaterialDialog, _: View, position: Int, _: CharSequence ->
                val newValue = entryValues.toList()[position].toString()
                if (callChangeListener(newValue)) {
                    persistString(key, newValue)
                    true
                } else {
                    false
                }
            }
            .show()
    }
}