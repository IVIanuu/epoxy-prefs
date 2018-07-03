package com.ivianuu.epoxyprefs

import android.content.Context
import android.widget.CompoundButton
import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.widget_preference_radio.*

/**
 * A radio button preference
 */
@EpoxyModelClass
abstract class RadioButtonPreferenceModel(context: Context) :
    CompoundButtonPreference(context) {

    override val Holder.compoundButton: CompoundButton?
        get() = radio

    init {
        widgetLayoutRes = R.layout.widget_preference_radio
    }

}