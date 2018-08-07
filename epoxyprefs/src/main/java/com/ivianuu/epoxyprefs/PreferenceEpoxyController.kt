package com.ivianuu.epoxyprefs

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView

/**
 * Preference epoxy controller
 */
abstract class PreferenceEpoxyController(
    val context: Context,
    val sharedPreferencesName: String? = EpoxyPrefsPlugins.getDefaultSharedPreferencesName(context)
) : EpoxyController() {

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val prefsChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            requestModelBuild()
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefsChangeListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefsChangeListener)
    }
}

fun Context.preferenceEpoxyController(buildModels: PreferenceEpoxyController.() -> Unit) =
    preferenceEpoxyController(this, buildModels)

@JvmName("preferenceEpoxyControllerWith")
fun preferenceEpoxyController(
    context: Context,
    buildModels: PreferenceEpoxyController.() -> Unit
): PreferenceEpoxyController {
    return object : PreferenceEpoxyController(context) {
        override fun buildModels() {
            buildModels.invoke(this)
        }
    }
}

fun EpoxyRecyclerView.buildPreferencesWith(buildModels: PreferenceEpoxyController.() -> Unit) {
    val controller = preferenceEpoxyController(context, buildModels)
    setControllerAndBuildModels(controller)
}