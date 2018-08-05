package com.ivianuu.epoxyprefs

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.airbnb.epoxy.EpoxyViewHolder

/**
 * Divider decoration for preferences
 */
class PreferenceDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    var divider: Drawable? = null
        set(value) {
            field = value
            dividerHeight = divider?.intrinsicHeight ?: 0
        }

    var dividerHeight = 0

    var style = Style.ITEMS

    init {
        val attrs =
            context.obtainStyledAttributes(intArrayOf(android.R.attr.dividerVertical))
        divider = attrs.getDrawable(0)
        attrs.recycle()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val divider = divider ?: return

        val childCount = parent.childCount
        val width = parent.width

        for (childViewIndex in 0 until childCount) {
            val view = parent.getChildAt(childViewIndex)
            if (shouldDrawDivider(view, parent)) {
                val top = view.y.toInt() + view.height
                divider.setBounds(0, top, width, top + dividerHeight)
                divider.draw(c)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (shouldDrawDivider(view, parent)) {
            outRect.bottom = dividerHeight
        }
    }

    private fun shouldDrawDivider(view: View, parent: RecyclerView): Boolean {
        val holder = parent.getChildViewHolder(view)

        val isCategory =
            holder is EpoxyViewHolder && holder.model is CategoryPreferenceModel

        if (isCategory) {
            // return true
        }

        val index = parent.indexOfChild(view)

        val isNextCategory = if (index < parent.childCount - 1) {
            val nextHolder = parent.getChildViewHolder(parent.getChildAt(index + 1))
            nextHolder is EpoxyViewHolder && nextHolder.model is CategoryPreferenceModel
        } else {
            false
        }

        if (isNextCategory) {
            return true
        }

        return false
    }

    enum class Style {
        CATEGORY, ITEMS
    }
}