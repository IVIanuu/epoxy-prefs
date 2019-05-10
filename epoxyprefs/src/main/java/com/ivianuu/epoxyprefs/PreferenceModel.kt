package com.ivianuu.epoxyprefs

import com.airbnb.epoxy.EpoxyController

fun EpoxyController.Preference(
    body: PreferenceModel.Builder.() -> Unit
): PreferenceModel = PreferenceModel.Builder()
    .injectContextIfPossible(this)
    .apply(body)
    .build()
    .also { it.addTo(this) }

/**
 * Simple Preference
 */
open class PreferenceModel(builder: Builder) : AbstractPreferenceModel<Nothing>(builder) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PreferenceModel) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    open class Builder : AbstractPreferenceModel.Builder<Nothing>() {
        override fun build() = PreferenceModel(this)
    }
}