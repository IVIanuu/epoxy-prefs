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

import android.widget.SeekBar
import com.airbnb.epoxy.EpoxyController
import kotlinx.android.synthetic.main.item_preference_seekbar.seekbar
import kotlinx.android.synthetic.main.item_preference_seekbar.seekbar_value
import kotlin.math.round

fun EpoxyController.SeekBarListPreference(
    body: SeekBarPreferenceModel.Builder.() -> Unit
): SeekBarPreferenceModel = SeekBarPreferenceModel.Builder()
    .apply(body)
    .build()
    .also { it.addTo(this) }

/**
 * Abstract seek bar Preference model
 */
open class SeekBarPreferenceModel(builder: Builder) : AbstractPreferenceModel<Int>(builder) {

    val min = builder.min
    val max = builder.max
    val incValue = builder.incValue
    val valueTextProvider = builder.valueTextProvider

    private var internalValue = 0

    override fun bind(holder: Holder) {
        super.bind(holder)

        internalValue = value ?: 0

        holder.seekbar.isEnabled = viewsShouldBeEnabled
        holder.seekbar.max = max - min
        holder.seekbar.progress = internalValue - min

        holder.seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) syncView(holder)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                persistValue(internalValue)
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

        internalValue = (round((progress / incValue).toDouble()) * incValue).toInt()

        val provider = valueTextProvider

        val text = provider?.invoke(internalValue)
            ?: internalValue.toString() // fallback

        holder.seekbar.progress = internalValue - min
        holder.seekbar_value.text = text
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SeekBarPreferenceModel) return false
        if (!super.equals(other)) return false

        if (min != other.min) return false
        if (max != other.max) return false
        if (incValue != other.incValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + min
        result = 31 * result + max
        result = 31 * result + incValue
        return result
    }

    open class Builder : AbstractPreferenceModel.Builder<Int>() {
        var min: Int = 0
            private set
        var max: Int = 100
            private set
        var incValue: Int = 1
            private set
        var valueTextProvider: ((Int) -> String)? = null
            private set

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

        fun valueTextProvider(valueTextProvider: ((Int) -> String)?) {
            this.valueTextProvider = valueTextProvider
        }

        override fun build() = SeekBarPreferenceModel(this)
    }
}