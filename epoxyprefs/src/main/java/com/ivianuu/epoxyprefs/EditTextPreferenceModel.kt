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
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModelClass

/**
 * A edit text preference
 */
@EpoxyModelClass
abstract class EditTextPreferenceModel(
    context: Context
) : DialogPreferenceModel(context) {

    @EpoxyAttribute var dialogHint: CharSequence? = null
    @EpoxyAttribute var allowEmptyInput: Boolean = true

    override fun showDialog() {
        val key = key ?: return

        val prefill = getPersistedString(key)

        MaterialDialog.Builder(context)
            .applyDialogSettings()
            .input(
                dialogHint ?: "",
                prefill,
                allowEmptyInput
            ) { _: MaterialDialog, input: CharSequence ->
                if (callChangeListener(input.toString())) {
                    persistString(key, input.toString())
                }
            }
            .show()
    }
}

open class EditTextPreferenceModelBuilder_(override val model: EditTextPreferenceModel) :
    DialogPreferenceModelBuilder_(model) {

    open fun dialogHint(dialogHint: CharSequence?) {
        model.dialogHint = dialogHint
    }

    open fun allowEmptyInput(allowEmptyInput: Boolean) {
        model.allowEmptyInput = allowEmptyInput
    }

}

fun EpoxyController.editTextPreference(
    context: Context,
    init: EditTextPreferenceModelBuilder_.() -> Unit
) {
    val model = EditTextPreferenceModel_(context)
    init.invoke(EditTextPreferenceModelBuilder_(model))
    model.addTo(this)
}

fun EditTextPreferenceModelBuilder_.dialogHintRes(dialogHintRes: Int) {
    dialogHint(model.context.getString(dialogHintRes))
}