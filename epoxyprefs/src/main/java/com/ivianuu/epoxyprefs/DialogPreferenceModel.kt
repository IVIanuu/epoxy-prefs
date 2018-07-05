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

/**
 * A dialog preference
 */
abstract class DialogPreferenceModel(builder: Builder) : PreferenceModel(builder) {

    open val dialogTitle = builder.dialogTitle ?: title
    open val dialogMessage = builder.dialogMessage
    open val dialogIcon = builder.dialogIcon
    open val positiveButtonText = builder.positiveButtonText
    open val negativeButtonText = builder.negativeButtonText

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DialogPreferenceModel) return false
        if (!super.equals(other)) return false

        if (dialogTitle != other.dialogTitle) return false
        if (dialogMessage != other.dialogMessage) return false
        if (dialogIcon != other.dialogIcon) return false
        if (positiveButtonText != other.positiveButtonText) return false
        if (negativeButtonText != other.negativeButtonText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (dialogTitle?.hashCode() ?: 0)
        result = 31 * result + (dialogMessage?.hashCode() ?: 0)
        result = 31 * result + (dialogIcon?.hashCode() ?: 0)
        result = 31 * result + (positiveButtonText?.hashCode() ?: 0)
        result = 31 * result + (negativeButtonText?.hashCode() ?: 0)
        return result
    }

    abstract class Builder(context: Context) : PreferenceModel.Builder(context) {

        open var dialogTitle: CharSequence? = null
        open var dialogMessage: CharSequence? = null
        open var dialogIcon: Drawable? = null
        open var positiveButtonText: CharSequence? =
            context.getString(android.R.string.ok)

        open var negativeButtonText: CharSequence? =
            context.getString(android.R.string.cancel)

        open fun dialogTitle(dialogTitle: CharSequence?) {
            this.dialogTitle = dialogTitle
        }

        open fun dialogMessage(dialogMessage: CharSequence?) {
            this.dialogMessage = dialogMessage
        }

        open fun dialogIcon(dialogIcon: Drawable?) {
            this.dialogIcon = dialogIcon
        }

        open fun positiveButtonText(positiveButtonText: CharSequence?) {
            this.positiveButtonText = positiveButtonText
        }

        open fun negativeButtonText(negativeButtonText: CharSequence?) {
            this.negativeButtonText = negativeButtonText
        }

    }
}

fun DialogPreferenceModel.Builder.dialogTitleRes(dialogTitleRes: Int) {
    dialogTitle(context.getString(dialogTitleRes))
}

fun DialogPreferenceModel.Builder.dialogMessageRes(dialogMessageRes: Int) {
    dialogTitle(context.getString(dialogMessageRes))
}

fun DialogPreferenceModel.Builder.dialogIconRes(dialogIconRes: Int) {
    dialogIcon(ContextCompat.getDrawable(context, dialogIconRes))
}

fun DialogPreferenceModel.Builder.positiveButtonTextRes(positiveButtonTextRes: Int) {
    positiveButtonText(context.getString(positiveButtonTextRes))
}

fun DialogPreferenceModel.Builder.negativeButtonTextRes(negativeButtonTextRes: Int) {
    negativeButtonText(context.getText(negativeButtonTextRes))
}