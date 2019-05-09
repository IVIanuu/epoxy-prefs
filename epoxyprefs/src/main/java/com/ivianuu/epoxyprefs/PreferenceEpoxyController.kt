package com.ivianuu.epoxyprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController

/**
 * Preference epoxy controller
 */
// todo remove once we came up with a better solution
abstract class PreferenceEpoxyController(
    val context: PreferenceContext
) : EpoxyController() {

    private val prefsChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            requestModelBuild()
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context.sharedPreferences
            .registerOnSharedPreferenceChangeListener(prefsChangeListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        context.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(prefsChangeListener)
    }
}

fun Context.preferenceEpoxyController(
    buildModels: PreferenceEpoxyController.() -> Unit
): PreferenceEpoxyController = preferenceEpoxyController(this, buildModels)

@JvmName("preferenceEpoxyControllerWith")
fun preferenceEpoxyController(
    context: Context,
    buildModels: PreferenceEpoxyController.() -> Unit
): PreferenceEpoxyController =
    preferenceEpoxyController(EpoxyPrefsPlugins.getDefaultContext(context), buildModels)

fun preferenceEpoxyController(
    context: PreferenceContext,
    buildModels: PreferenceEpoxyController.() -> Unit
): PreferenceEpoxyController {
    return object : PreferenceEpoxyController(context) {
        override fun buildModels() {
            buildModels.invoke(this)
        }
    }
}