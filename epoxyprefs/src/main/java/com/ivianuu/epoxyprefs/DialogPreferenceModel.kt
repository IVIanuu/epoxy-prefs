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
import com.afollestad.materialdialogs.MaterialDialog

abstract class DialogPreferenceModel<T : Any>(builder: Builder<T>) :
    AbstractPreferenceModel<T>(builder) {

    val dialogTitle = builder.dialogTitle
    val dialogTitleRes = builder.dialogTitleRes
    val dialogMessage = builder.dialogMessage
    val dialogMessageRes = builder.dialogMessageRes
    val dialogIcon = builder.dialogIcon
    val dialogIconRes = builder.dialogIconRes
    val positiveButtonText = builder.positiveButtonText
    val positiveButtonTextRes = builder.positiveButtonTextRes
    val negativeButtonText = builder.negativeButtonText
    val negativeButtonTextRes = builder.negativeButtonTextRes

    private lateinit var androidContext: Context

    override fun bind(holder: Holder) {
        androidContext = holder.containerView.context
        super.bind(holder)
    }

    override fun onClick() {
        super.onClick()
        showDialog(androidContext)
    }

    protected abstract fun showDialog(context: Context)

    protected fun MaterialDialog.applyDialogSettings(
        applyTitle: Boolean = true,
        applyMessage: Boolean = true,
        applyIcon: Boolean = true,
        applyPositiveButtonText: Boolean = true,
        applyNegativeButtonText: Boolean = true
    ) = apply {
        if (applyTitle && (dialogTitleRes != 0 || dialogTitle != null)) title(
            res = dialogTitleRes,
            text = dialogTitle
        )
        if (applyMessage && (dialogMessageRes != 0 || dialogMessage != null)) message(
            res = dialogMessageRes,
            text = dialogMessage
        )
        if (applyIcon && (dialogIconRes != 0 || dialogIcon != null)) icon(
            res = dialogIconRes,
            drawable = dialogIcon
        )
        if (applyPositiveButtonText && (positiveButtonTextRes != 0 || positiveButtonText != null)) positiveButton(
            res = positiveButtonTextRes,
            text = positiveButtonText
        )
        if (applyNegativeButtonText && (negativeButtonTextRes != 0 || negativeButtonText != null)) negativeButton(
            res = negativeButtonTextRes,
            text = negativeButtonText
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DialogPreferenceModel<*>) return false
        if (!super.equals(other)) return false

        if (dialogTitle != other.dialogTitle) return false
        if (dialogTitleRes != other.dialogTitleRes) return false
        if (dialogMessage != other.dialogMessage) return false
        if (dialogMessageRes != other.dialogMessageRes) return false
        if (dialogIcon != other.dialogIcon) return false
        if (dialogIconRes != other.dialogIconRes) return false
        if (positiveButtonText != other.positiveButtonText) return false
        if (positiveButtonTextRes != other.positiveButtonTextRes) return false
        if (negativeButtonText != other.negativeButtonText) return false
        if (negativeButtonTextRes != other.negativeButtonTextRes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (dialogTitle?.hashCode() ?: 0)
        result = 31 * result + dialogTitleRes
        result = 31 * result + (dialogMessage?.hashCode() ?: 0)
        result = 31 * result + dialogMessageRes
        result = 31 * result + (dialogIcon?.hashCode() ?: 0)
        result = 31 * result + dialogIconRes
        result = 31 * result + (positiveButtonText?.hashCode() ?: 0)
        result = 31 * result + positiveButtonTextRes
        result = 31 * result + (negativeButtonText?.hashCode() ?: 0)
        result = 31 * result + negativeButtonTextRes
        return result
    }

    abstract class Builder<T : Any> : AbstractPreferenceModel.Builder<T>() {

        internal var dialogTitle: String? by lazyVar { title }
            private set
        internal var dialogTitleRes: Int by lazyVar { titleRes }
            private set
        internal var dialogMessage: String? = null
            private set
        internal var dialogMessageRes: Int = 0
            private set
        internal var dialogIcon: Drawable? = null
            private set
        internal var dialogIconRes: Int = 0
            private set
        internal var positiveButtonText: String? = null
            private set
        internal var positiveButtonTextRes: Int = android.R.string.ok
            private set
        internal var negativeButtonText: String? = null
            private set
        internal var negativeButtonTextRes: Int = android.R.string.cancel
            private set

        fun dialogTitle(dialogTitle: String?) {
            this.dialogTitle = dialogTitle
        }

        fun dialogTitleRes(dialogTitleRes: Int) {
            this.dialogTitleRes = dialogTitleRes
        }

        fun dialogMessage(dialogMessage: String?) {
            this.dialogMessage = dialogMessage
        }

        fun dialogMessageRes(dialogMessageRes: Int) {
            this.dialogMessageRes = dialogMessageRes
        }

        fun dialogIcon(dialogIcon: Drawable?) {
            this.dialogIcon = dialogIcon
        }

        fun dialogIconRes(dialogIconRes: Int) {
            this.dialogIconRes = dialogIconRes
        }

        fun positiveButtonText(positiveButtonText: String?) {
            this.positiveButtonText = positiveButtonText
        }

        fun positiveButtonTextRes(positiveButtonTextRes: Int) {
            this.positiveButtonTextRes = positiveButtonTextRes
        }

        fun negativeButtonText(negativeButtonText: String?) {
            this.negativeButtonText = negativeButtonText
        }

        fun negativeButtonTextRes(negativeButtonTextRes: Int) {
            this.negativeButtonTextRes = negativeButtonTextRes
        }

        abstract override fun build(): DialogPreferenceModel<T>
    }

}