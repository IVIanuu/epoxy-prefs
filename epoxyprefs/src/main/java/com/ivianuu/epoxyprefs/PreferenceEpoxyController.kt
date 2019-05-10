package com.ivianuu.epoxyprefs

import android.content.Context
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController

/**
 * Preference epoxy controller
 */
// todo remove once we came up with a better solution
abstract class PreferenceEpoxyController(
    val context: PreferenceContext
) : EpoxyController() {

    private val prefsChangeListener: (String) -> Unit = {
        println("key changed $it")
        requestModelBuild()
    }

    init {
        addModelBuildListener {
            println("models build done $it")

            println("changes--start")
            it.dispatchTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    println("on changed pos $position c $count")
                }

                override fun onInserted(position: Int, count: Int) {
                    println("on inserted pos $position c $count")
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    println("on moved from $fromPosition to $toPosition")
                }

                override fun onRemoved(position: Int, count: Int) {
                    println("on removed pos $position c $count")
                }
            })
            println("changes--end")
        }
    }

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
            println("build models ")
            buildModels.invoke(this)
        }
    }
}