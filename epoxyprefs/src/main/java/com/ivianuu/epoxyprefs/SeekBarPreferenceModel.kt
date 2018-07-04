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
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.item_preference_seekbar.*

/**
 * Abstract seek bar preference model
 */
@EpoxyModelClass
abstract class SeekBarPreferenceModel(
    context: Context
) : PreferenceModel(context) {

    @EpoxyAttribute var min: Int = 0
    @EpoxyAttribute var max: Int = 0
    @EpoxyAttribute var incValue: Int = 1

    @EpoxyAttribute var valueTextProvider: ValueTextProvider? = null

    protected open val PreferenceModel.Holder.seekBar: SeekBar? get() = seekbar
    protected open val PreferenceModel.Holder.seekBarValue: TextView? get() = seekbar_value

    private var internalValue: Int = 0

    init {
        layoutRes = R.layout.item_preference_seekbar
    }

    override fun bind(holder: PreferenceModel.Holder) {
        super.bind(holder)
        val seekBar = holder.seekBar ?: return

        internalValue = getPersistedInt(key)

        seekBar.max = max - min
        seekBar.progress = internalValue - min

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) syncView()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (callChangeListener(internalValue)) {
                    persistInt(key, internalValue)
                }
            }
        })

        syncView()
    }

    private fun syncView() {
        val seekBar = currentHolder?.seekBar ?: return
        val seekBarValue = currentHolder?.seekBarValue ?: return

        var progress = min + seekBar.progress

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

        seekBar.progress = internalValue - min
        seekBarValue.text = text
    }

    interface ValueTextProvider {
        fun getText(value: Int): String
    }

    open class Builder(override val model: SeekBarPreferenceModel) :
        PreferenceModel.Builder(model) {

        open fun min(min: Int) {
            model.min = min
        }

        open fun max(max: Int) {
            model.max = max
        }

        open fun incValue(incValue: Int) {
            model.incValue = incValue
        }

        open fun valueTextProvider(valueTextProvider: SeekBarPreferenceModel.ValueTextProvider) {
            model.valueTextProvider = valueTextProvider
        }

    }
}

inline fun EpoxyController.seekBarPreference(
    context: Context,
    init: SeekBarPreferenceModel.Builder.() -> Unit
) {
    val model = SeekBarPreferenceModel_(context)
    init.invoke(SeekBarPreferenceModel.Builder(model))
    model.addTo(this)
}

inline fun PreferenceEpoxyController.seekBarPreference(
    init: SeekBarPreferenceModel.Builder.() -> Unit
) {
    seekBarPreference(context, init)
}

fun SeekBarPreferenceModel.Builder.valueTextProvider(getText: (Int) -> String) {
    valueTextProvider(object : SeekBarPreferenceModel.ValueTextProvider {
        override fun getText(value: Int) = getText(value)
    })
}