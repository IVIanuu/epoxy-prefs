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
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.airbnb.epoxy.EpoxyController

fun EpoxyController.SingleItemListPreference(
    body: SingleItemListPreferenceModel.Builder.() -> Unit
): SingleItemListPreferenceModel = SingleItemListPreferenceModel.Builder()
    .apply(body)
    .build()
    .also { it.addTo(this) }


/**
 * A single item Preference
 */
open class SingleItemListPreferenceModel(builder: Builder) : ListPreferenceModel<String>(builder) {

    override fun showDialog(context: Context) {
        val entries = entries ?: emptyArray()
        val entryValues = entryValues ?: emptyArray()

        val currentValue = value ?: ""
        val selectedIndex = entryValues.indexOf(currentValue)

        MaterialDialog(context)
            .applyDialogSettings(applyPositiveButtonText = false)
            .listItemsSingleChoice(
                initialSelection = selectedIndex,
                items = entries.toList(),
                waitForPositiveButton = false
            ) { dialog, position, _ ->
                val newValue = entryValues.toList()[position]
                persistValue(newValue)
                dialog.dismiss()
            }
            .show()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SingleItemListPreferenceModel) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    open class Builder : ListPreferenceModel.Builder<String>() {
        override fun build() = SingleItemListPreferenceModel(this)
    }
}