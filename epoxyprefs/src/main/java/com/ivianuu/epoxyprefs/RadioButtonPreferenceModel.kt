package com.ivianuu.epoxyprefs

import android.content.Context
import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.widget_preference_radio.*

/**
 * A radio button preference
 */
@EpoxyModelClass
abstract class RadioButtonPreferenceModel(context: Context) :
    CompoundButtonPreferenceModel(context) {

    override val Holder.compoundButton: CompoundButton?
        get() = radio

    init {
        widgetLayoutRes = R.layout.widget_preference_radio
    }
}

open class RadioButtonPreferenceModelBuilder_(override val model: RadioButtonPreferenceModel) :
    CompoundButtonPreferenceModelBuilder_(model)

fun EpoxyController.radioButtonPreference(
    context: Context,
    init: RadioButtonPreferenceModelBuilder_.() -> Unit
) {
    val model = RadioButtonPreferenceModel_(context)
    init.invoke(RadioButtonPreferenceModelBuilder_(model))
    model.addTo(this)
}