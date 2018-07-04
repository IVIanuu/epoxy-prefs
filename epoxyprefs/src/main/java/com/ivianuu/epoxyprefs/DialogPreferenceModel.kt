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
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.epoxy.EpoxyAttribute

/**
 * A dialog preference
 */
abstract class DialogPreferenceModel(
    context: Context
) : PreferenceModel(context) {

    @EpoxyAttribute var dialogTitle: CharSequence? = null
        get() {
            return field ?: title // try to use the title if the field is null
        }
    @EpoxyAttribute var dialogMessage: CharSequence? = null
    @EpoxyAttribute var dialogIcon: Drawable? = null
    @EpoxyAttribute var positiveButtonText: CharSequence? =
        context.getString(android.R.string.ok)

    @EpoxyAttribute var negativeButtonText: CharSequence? =
        context.getString(android.R.string.cancel)

    override fun onClick() {
        super.onClick()
        showDialog()
    }

    protected abstract fun showDialog()

    protected open fun MaterialDialog.Builder.applyDialogSettings(
        applyTitle: Boolean = true,
        applyMessage: Boolean = true,
        applyIcon: Boolean = true,
        applyPositiveButtonText: Boolean = true,
        applyNegativeButtonText: Boolean = true
    ): MaterialDialog.Builder {
        if (applyTitle) dialogTitle?.let(this::title)
        if (applyMessage) dialogMessage?.let(this::content)
        if (applyIcon) dialogIcon?.let(this::icon)
        if (applyPositiveButtonText) positiveButtonText?.let(this::positiveText)
        if (applyNegativeButtonText) negativeButtonText?.let(this::negativeText)
        return this
    }

    abstract class Builder(override val model: DialogPreferenceModel) :
        PreferenceModel.Builder(model) {

        fun dialogTitle(dialogTitle: CharSequence?) {
            model.dialogTitle = dialogTitle
        }

        fun dialogMessage(dialogMessage: CharSequence?) {
            model.dialogMessage = dialogMessage
        }

        fun dialogIcon(dialogIcon: Drawable?) {
            model.dialogIcon = dialogIcon
        }

        fun positiveButtonText(positiveButtonText: CharSequence?) {
            model.positiveButtonText = positiveButtonText
        }

        fun negativeButtonText(negativeButtonText: CharSequence?) {
            model.negativeButtonText = negativeButtonText
        }

    }
}

fun DialogPreferenceModel.Builder.dialogTitleRes(dialogTitleRes: Int) {
    dialogTitle(model.context.getString(dialogTitleRes))
}

fun DialogPreferenceModel.Builder.dialogMessageRes(dialogMessageRes: Int) {
    dialogTitle(model.context.getString(dialogMessageRes))
}

fun DialogPreferenceModel.Builder.dialogIconRes(dialogIconRes: Int) {
    dialogIcon(ContextCompat.getDrawable(model.context, dialogIconRes))
}

fun DialogPreferenceModel.Builder.positiveButtonTextRes(positiveButtonTextRes: Int) {
    positiveButtonText(model.context.getString(positiveButtonTextRes))
}

fun DialogPreferenceModel.Builder.negativeButtonTextRes(negativeButtonTextRes: Int) {
    negativeButtonText(model.context.getText(negativeButtonTextRes))
}