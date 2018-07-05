package com.ivianuu.epoxyprefs

import android.content.Context
import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyController
import kotlinx.android.synthetic.main.widget_preference_radio.*

/**
 * A radio button preference
 */
open class RadioButtonPreferenceModel(builder: Builder) : CompoundButtonPreferenceModel(builder) {

    override val Holder.compoundButton: CompoundButton?
        get() = radio

    open class Builder(context: Context) : CompoundButtonPreferenceModel.Builder(context) {

        init {
            widgetLayoutRes(R.layout.widget_preference_radio)
        }

        override fun build() = RadioButtonPreferenceModel(this)

    }
}

inline fun EpoxyController.radioButtonPreference(
    context: Context,
    init: RadioButtonPreferenceModel.Builder.() -> Unit
) = RadioButtonPreferenceModel.Builder(context)
    .apply(init)
    .build()
    .also { it.addTo(this) }

inline fun PreferenceEpoxyController.radioButtonPreference(
    init: RadioButtonPreferenceModel.Builder.() -> Unit
) = radioButtonPreference(context, init)