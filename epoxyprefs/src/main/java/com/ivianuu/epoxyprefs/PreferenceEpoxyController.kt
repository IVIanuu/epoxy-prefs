package com.ivianuu.epoxyprefs

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController

// todo remove once we came up with a better solution
abstract class PreferenceEpoxyController(
    val context: PreferenceContext
) : EpoxyController() {

    private val prefsChangeListener: (String) -> Unit = { requestModelBuild() }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context.addChangeListener(prefsChangeListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        context.removeChangeListener(prefsChangeListener)
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