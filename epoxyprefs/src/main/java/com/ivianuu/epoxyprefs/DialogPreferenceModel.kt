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
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog

/**
 * A dialog preference
 */
abstract class DialogPreferenceModel(builder: Builder) : PreferenceModel(builder) {

    val dialogTitle = builder.dialogTitle ?: title
    val dialogMessage = builder.dialogMessage
    val dialogIcon = builder.dialogIcon
    val positiveButtonText = builder.positiveButtonText
    val negativeButtonText = builder.negativeButtonText

    override fun onClick() {
        super.onClick()
        showDialog()
    }

    protected abstract fun showDialog()

    protected open fun MaterialDialog.applyDialogSettings(
        applyTitle: Boolean = true,
        applyMessage: Boolean = true,
        applyIcon: Boolean = true,
        applyPositiveButtonText: Boolean = true,
        applyNegativeButtonText: Boolean = true
    ) = apply {
        if (applyTitle) dialogTitle?.let { title(text = it) }
        if (applyMessage) dialogMessage?.let { message(text = it) }
        if (applyIcon) dialogIcon?.let { icon(drawable = it) }
        if (applyPositiveButtonText) positiveButtonText?.let { positiveButton(text = it) }
        if (applyNegativeButtonText) negativeButtonText?.let { negativeButton(text = it) }
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

        var dialogTitle: String? = null
            private set
        var dialogMessage: String? = null
            private set
        var dialogIcon: Drawable? = null
            private set
        var positiveButtonText: String? =
            context.getString(android.R.string.ok)
            private set

        var negativeButtonText: String? =
            context.getString(android.R.string.cancel)
            private set

        fun dialogTitle(dialogTitle: String?) {
            this.dialogTitle = dialogTitle
        }

        fun dialogMessage(dialogMessage: String?) {
            this.dialogMessage = dialogMessage
        }

        fun dialogIcon(dialogIcon: Drawable?) {
            this.dialogIcon = dialogIcon
        }

        fun positiveButtonText(positiveButtonText: String?) {
            this.positiveButtonText = positiveButtonText
        }

        fun negativeButtonText(negativeButtonText: String?) {
            this.negativeButtonText = negativeButtonText
        }

    }
}

fun DialogPreferenceModel.Builder.dialogTitle(dialogTitleRes: Int) {
    dialogTitle(context.getString(dialogTitleRes))
}

fun DialogPreferenceModel.Builder.dialogMessage(dialogMessageRes: Int) {
    dialogTitle(context.getString(dialogMessageRes))
}

fun DialogPreferenceModel.Builder.dialogIcon(dialogIconRes: Int) {
    dialogIcon(ContextCompat.getDrawable(context, dialogIconRes))
}

fun DialogPreferenceModel.Builder.positiveButtonText(positiveButtonTextRes: Int) {
    positiveButtonText(context.getString(positiveButtonTextRes))
}

fun DialogPreferenceModel.Builder.negativeButtonText(negativeButtonTextRes: Int) {
    negativeButtonText(context.getString(negativeButtonTextRes))
}