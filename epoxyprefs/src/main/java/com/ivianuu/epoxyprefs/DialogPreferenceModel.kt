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
    @EpoxyAttribute var positiveButtonText: CharSequence? = null
    @EpoxyAttribute var negativeButtonText: CharSequence? = null

    override fun onClick() {
        super.onClick()
        showDialog()
    }

    fun dialogTitle(dialogTitle: CharSequence?) {
        this.dialogTitle = dialogTitle
    }

    fun dialogMessage(dialogMessage: CharSequence?) {
        this.dialogMessage = dialogMessage
    }

    fun dialogIcon(dialogIcon: Drawable?) {
        this.dialogIcon = dialogIcon
    }

    fun positiveButtonText(positiveButtonText: CharSequence?) {
        this.positiveButtonText = positiveButtonText
    }

    fun negativeButtonText(negativeButtonText: CharSequence?) {
        this.negativeButtonText = negativeButtonText
    }

    protected abstract fun showDialog()

    protected open fun MaterialDialog.Builder.applyDialogSettings(): MaterialDialog.Builder {
        dialogTitle?.let(this::title)
        dialogMessage?.let(this::content)
        dialogIcon?.let(this::icon)
        positiveButtonText?.let(this::positiveText)
        negativeButtonText?.let(this::negativeText)
        return this
    }
}

fun DialogPreferenceModel.dialogTitleRes(dialogTitleRes: Int) {
    dialogTitle(context.getString(dialogTitleRes))
}

fun DialogPreferenceModel.dialogMessageRes(dialogMessageRes: Int) {
    dialogTitle(context.getString(dialogMessageRes))
}

fun DialogPreferenceModel.dialogIconRes(dialogIconRes: Int) {
    dialogIcon(ContextCompat.getDrawable(context, dialogIconRes))
}

fun DialogPreferenceModel.positiveButtonText(positiveButtonTextRes: Int) {
    positiveButtonText(context.getString(positiveButtonTextRes))
}

fun DialogPreferenceModel.negativeButtonText(negativeButtonTextRes: Int) {
    negativeButtonText(context.getText(negativeButtonTextRes))
}