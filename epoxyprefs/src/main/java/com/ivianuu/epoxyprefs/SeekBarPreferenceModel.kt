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
import android.widget.SeekBar
import com.airbnb.epoxy.EpoxyController
import com.ivianuu.epoxyprefs.internal.setTextFuture
import kotlinx.android.synthetic.main.item_preference_seekbar.*

/**
 * Abstract seek bar preference model
 */
open class SeekBarPreferenceModel(builder: Builder) : PreferenceModel(builder) {

    val min = builder.min
    val max = builder.max
    val incValue = builder.incValue

    val valueTextProvider = builder.valueTextProvider

    private var internalValue = 0

    override fun bind(holder: PreferenceModel.Holder) {
        super.bind(holder)

        internalValue = value as? Int ?: 0

        holder.seekbar.max = max - min
        holder.seekbar.progress = internalValue - min

        holder.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) syncView(holder)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (callChangeListener(internalValue)) {
                    persistInt(key, internalValue)
                }
            }
        })

        syncView(holder)
    }

    private fun syncView(holder: Holder) {
        var progress = min + holder.seekbar.progress

        if (progress < min) {
            progress = min
        }

        if (progress > max) {
            progress = max
        }

        internalValue = (Math.round((progress / incValue).toDouble()) * incValue).toInt()

        val provider = valueTextProvider

        val text = provider?.getText(internalValue)
                ?: internalValue.toString() // fallback

        holder.seekbar.progress = internalValue - min
        holder.seekbar_value.setTextFuture(text)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SeekBarPreferenceModel) return false
        if (!super.equals(other)) return false

        if (min != other.min) return false
        if (max != other.max) return false
        if (incValue != other.incValue) return false
        if (valueTextProvider != other.valueTextProvider) return false
        if (internalValue != other.internalValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + min
        result = 31 * result + max
        result = 31 * result + incValue
        result = 31 * result + (valueTextProvider?.hashCode() ?: 0)
        result = 31 * result + internalValue
        return result
    }

    interface ValueTextProvider {
        fun getText(value: Int): String
    }

    open class Builder(context: Context) : PreferenceModel.Builder(context) {

        var min: Int = 0
        var max: Int = 0
        var incValue: Int = 1

        var valueTextProvider: ValueTextProvider? = null

        init {
            layoutRes(R.layout.item_preference_seekbar)
        }

        fun min(min: Int) {
            this.min = min
        }

        fun max(max: Int) {
            this.max = max
        }

        fun incValue(incValue: Int) {
            this.incValue = incValue
        }

        fun valueTextProvider(valueTextProvider: SeekBarPreferenceModel.ValueTextProvider) {
            this.valueTextProvider = valueTextProvider
        }

        override fun build() = SeekBarPreferenceModel(this)
    }
}

inline fun EpoxyController.seekBarPreference(
    context: Context,
    init: SeekBarPreferenceModel.Builder.() -> Unit
) = SeekBarPreferenceModel.Builder(context)
    .apply(init)
    .build()
    .also { it.addTo(this) }

inline fun PreferenceEpoxyController.seekBarPreference(
    init: SeekBarPreferenceModel.Builder.() -> Unit
) = seekBarPreference(context, init)

fun SeekBarPreferenceModel.Builder.valueTextProvider(getText: (Int) -> String) {
    valueTextProvider(object : SeekBarPreferenceModel.ValueTextProvider {
        override fun getText(value: Int) = getText(value)
    })
}