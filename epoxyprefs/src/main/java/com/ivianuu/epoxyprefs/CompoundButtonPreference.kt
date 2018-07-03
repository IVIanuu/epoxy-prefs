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
import android.widget.CompoundButton

/**
 * A preference for compound buttons
 */
abstract class CompoundButtonPreferenceModel(
    context: Context
) : PreferenceModel(context) {

    protected abstract val PreferenceModel.Holder.compoundButton: CompoundButton?

    override fun bind(holder: PreferenceModel.Holder) {
        super.bind(holder)

        val key = key ?: return

        holder.compoundButton?.let { compoundButton ->
            compoundButton.setOnCheckedChangeListener { _, checked ->
                if (callChangeListener(checked)) {
                    persistBoolean(key, checked)
                } else {
                    compoundButton.isChecked = !checked
                }
            }
        }

        syncValueWithCompoundButton()
    }

    override fun unbind(holder: PreferenceModel.Holder) {
        super.unbind(holder)
        holder.compoundButton?.setOnCheckedChangeListener(null)
    }

    override fun onClick() {
        super.onClick()
        val key = key ?: return
        val newValue = !getPersistedBoolean(key)
        if (callChangeListener(newValue)) {
            persistBoolean(key, newValue)
        }
    }

    override fun onChange() {
        super.onChange()
        syncValueWithCompoundButton()
    }

    private fun syncValueWithCompoundButton() {
        val key = key ?: return
        currentHolder?.compoundButton?.isChecked = getPersistedBoolean(key)
    }

}

abstract class CompoundButtonPreferenceModelBuilder_(override val model: CompoundButtonPreferenceModel) :
    PreferenceModelBuilder_(model)