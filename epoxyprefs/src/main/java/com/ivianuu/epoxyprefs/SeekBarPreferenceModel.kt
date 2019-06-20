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

import android.view.View
import android.widget.SeekBar
import com.airbnb.epoxy.EpoxyController
import kotlinx.android.synthetic.main.item_preference_seekbar.*
import kotlin.math.round

fun EpoxyController.SeekBarPreference(
    body: SeekBarPreferenceModel.Builder.() -> Unit
): SeekBarPreferenceModel = SeekBarPreferenceModel.Builder()
    .injectContextIfPossible(this)
    .apply(body)
    .build()
    .also { it.addTo(this) }

open class SeekBarPreferenceModel(builder: Builder) : AbstractPreferenceModel<Int>(builder) {

    val min = builder.min
    val max = builder.max
    val incValue = builder.incValue
    val showSeekBarValue = builder.showSeekBarValue
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

        holder.seekbar.progress = internalValue - min

        if (showSeekBarValue) {
            val provider = valueTextProvider

            val text = provider?.invoke(internalValue)
                ?: internalValue.toString() // fallback
            holder.seekbar_value.visibility = View.VISIBLE
            holder.seekbar_value.text = text
        } else {
            holder.seekbar_value.visibility = View.GONE
            holder.seekbar_value.text = null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SeekBarPreferenceModel) return false
        if (!super.equals(other)) return false

        if (min != other.min) return false
        if (max != other.max) return false
        if (showSeekBarValue != other.showSeekBarValue) return false
        if (incValue != other.incValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + min
        result = 31 * result + max
        result = 31 * result + showSeekBarValue.hashCode()
        result = 31 * result + incValue
        return result
    }

    open class Builder : AbstractPreferenceModel.Builder<Int>() {
        internal var min: Int = 0
            private set
        internal var max: Int = 100
            private set
        internal var incValue: Int = 1
            private set
        internal var showSeekBarValue: Boolean = true
            private set
        internal var valueTextProvider: ((Int) -> String)? = null
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

        fun showSeekBarValue(showSeekBarValue: Boolean) {
            this.showSeekBarValue = showSeekBarValue
        }

        fun valueTextProvider(valueTextProvider: ((Int) -> String)?) {
            this.valueTextProvider = valueTextProvider
        }

        override fun build() = SeekBarPreferenceModel(this)
    }
}