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
import com.afollestad.materialdialogs.input.input
import com.airbnb.epoxy.EpoxyController

/**
 * A edit text preference
 */
open class EditTextPreferenceModel(builder: Builder) : DialogPreferenceModel<String>(builder) {

    val dialogHint = builder.dialogHint

    override fun showDialog() {
        val prefill = value ?: ""

        MaterialDialog(context)
            .applyDialogSettings()
            .input(
                hint = dialogHint ?: "",
                prefill = prefill
            ) { _, input -> persistValue(input.toString()) }
            .show()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EditTextPreferenceModel) return false
        if (!super.equals(other)) return false

        if (dialogHint != other.dialogHint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (dialogHint?.hashCode() ?: 0)
        return result
    }

    open class Builder(context: Context) : DialogPreferenceModel.Builder<String>(context) {

        var dialogHint: String? = null
            private set


        fun dialogHint(dialogHint: String?) {
            this.dialogHint = dialogHint
        }

        override fun build(): EditTextPreferenceModel = EditTextPreferenceModel(this)
    }
}

inline fun EpoxyController.editTextPreference(
    context: Context,
    init: EditTextPreferenceModel.Builder.() -> Unit
): EditTextPreferenceModel {
    return EditTextPreferenceModel.Builder(context)
        .apply(init)
        .build()
        .also { it.addTo(this) }
}

inline fun PreferenceEpoxyController.editTextPreference(
    init: EditTextPreferenceModel.Builder.() -> Unit
): EditTextPreferenceModel = editTextPreference(context, init)

fun EditTextPreferenceModel.Builder.dialogHint(dialogHintRes: Int) {
    dialogHint(context.getString(dialogHintRes))
}