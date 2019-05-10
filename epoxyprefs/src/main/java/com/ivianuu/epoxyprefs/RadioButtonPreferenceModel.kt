package com.ivianuu.epoxyprefs

import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyController
import kotlinx.android.synthetic.main.widget_preference_radio.radio

fun EpoxyController.RadioButtonPreference(
    body: RadioButtonPreferenceModel.Builder.() -> Unit
): RadioButtonPreferenceModel = RadioButtonPreferenceModel.Builder()
    .apply(body)
    .build()
    .also { it.addTo(this) }

/**
 * A check box Preference
 */
open class RadioButtonPreferenceModel(builder: CompoundButtonPreferenceModel.Builder) :
    CompoundButtonPreferenceModel(builder) {

    override val Holder.compoundButton: CompoundButton?
        get() = radio

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RadioButtonPreferenceModel) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    open class Builder : CompoundButtonPreferenceModel.Builder() {
        init {
            widgetLayoutRes(R.layout.widget_preference_radio)
        }

        override fun build() = RadioButtonPreferenceModel(this)
    }
}