package com.ivianuu.epoxyprefs

import android.content.Context
import com.airbnb.epoxy.EpoxyController

/**
 * Simple preference
 */
open class PreferenceModel internal constructor(builder: Builder) :
    AbstractPreferenceModel<Nothing>(builder) {
    open class Builder(context: Context) : AbstractPreferenceModel.Builder<Nothing>(context) {
        override fun build(): PreferenceModel = PreferenceModel(this)
    }
}

inline fun EpoxyController.preference(
    context: Context,
    init: PreferenceModel.Builder.() -> Unit
): PreferenceModel {
    return PreferenceModel.Builder(context)
        .apply(init)
        .build()
        .also { it.addTo(this) }
}

inline fun PreferenceEpoxyController.preference(init: PreferenceModel.Builder.() -> Unit): PreferenceModel =
    preference(context, init)

