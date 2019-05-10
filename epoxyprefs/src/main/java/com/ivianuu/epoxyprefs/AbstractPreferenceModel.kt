/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.epoxyprefs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_preference.*
import kotlin.properties.Delegates

data class PreferenceDependency<T : Any>(
    val key: String,
    val value: T,
    val defaultValue: T? = value.tryToResolveDefaultValue() as? T
) {
    fun isOk(context: PreferenceContext): Boolean {
        return (if (defaultValue != null) {
            context.getOrDefault(key, defaultValue)
        } else {
            context.get<Any>(key)
        }) ?: value.tryToResolveDefaultValue() == value
    }
}

/**
 * Base Preference
 */
abstract class AbstractPreferenceModel<T : Any>(
    builder: Builder<T>
) : EpoxyModelWithHolder<AbstractPreferenceModel.Holder>() {

    val key = builder.key
    val title = builder.title
    val titleRes = builder.titleRes
    val summary = builder.summary
    val summaryRes = builder.summaryRes
    val icon = builder.icon
    val iconRes = builder.iconRes
    val preserveIconSpace = builder.preserveIconSpace
    val defaultValue = builder.defaultValue
    val enabled = builder.isEnabled
    val isClickable = builder.isClickable
    val dependencies: List<PreferenceDependency<*>> = builder.dependencies
    val onClick = builder.onClick
    val onChange = builder.onChange
    val persistent = builder.isPersistent
    val layoutRes = builder.layoutRes
    val widgetLayoutRes = builder.widgetLayoutRes
    val context = builder.context

    private lateinit var androidContext: Context

    val _context by lazy(LazyThreadSafetyMode.NONE) {
        context ?: EpoxyPrefsPlugins.getDefaultContext(androidContext)
    }

    val allowedByDependencies by lazy(LazyThreadSafetyMode.NONE) {
        dependencies.all { it.isOk(_context) }
    }

    val value by lazy(LazyThreadSafetyMode.NONE) {
        if (persistent) {
            if (defaultValue != null) {
                _context.getOrDefault(key, defaultValue)
            } else {
                _context.get<T>(key)
            }
        } else if (!persistent) {
            defaultValue
        } else {
            null
        }
    }

    protected val viewsShouldBeEnabled: Boolean get() = enabled && allowedByDependencies

    init {
        id(key)
        layout(layoutRes + widgetLayoutRes)
    }

    @CallSuper
    override fun bind(holder: Holder) {
        androidContext = holder.containerView.context
        super.bind(holder)

        holder.title?.let {
            it.text = title
            it.visibility = if (title != null) View.VISIBLE else View.GONE
            it.isEnabled = viewsShouldBeEnabled
        }

        holder.summary?.let {
            it.text = summary
            it.visibility = if (summary != null) View.VISIBLE else View.GONE
            it.isEnabled = viewsShouldBeEnabled
        }

        holder.icon?.let {
            it.setImageDrawable(icon)
            it.isEnabled = viewsShouldBeEnabled
        }

        holder.icon_frame?.let {
            it.visibility = if (icon != null || preserveIconSpace) View.VISIBLE else View.GONE
            it.isEnabled = viewsShouldBeEnabled
        }

        holder.widget_frame?.let { widgetFrame ->
            widgetFrame.isEnabled = viewsShouldBeEnabled
            (0 until widgetFrame.childCount)
                .map { widgetFrame.getChildAt(it) }
                .forEach { it.isEnabled = viewsShouldBeEnabled }
        }

        holder.containerView.apply {
            isEnabled = viewsShouldBeEnabled
            isClickable = this@AbstractPreferenceModel.isClickable

            if (this@AbstractPreferenceModel.isClickable) {
                setOnClickListener {
                    val handled = onClick?.invoke() ?: false
                    if (!handled) {
                        onClick()
                    }
                }
            } else {
                setOnClickListener(null)
            }
        }
    }

    override fun createNewHolder() = Holder()

    override fun getDefaultLayout() = R.layout.item_preference

    override fun buildView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutRes, parent, false)

        val widgetFrame = view.findViewById<ViewGroup>(android.R.id.widget_frame)

        if (widgetFrame != null) {
            if (widgetLayoutRes != 0) {
                inflater.inflate(widgetLayoutRes, widgetFrame)
            } else {
                widgetFrame.visibility = View.GONE
            }
        }

        return view
    }

    protected open fun onClick() {
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("ApplySharedPref")
    protected fun persistValue(value: T) {
        if (onChange?.invoke(value) != false && persistent) {
            _context.set(key, value)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractPreferenceModel<*>) return false
        if (!super.equals(other)) return false

        if (key != other.key) return false
        if (title != other.title) return false
        if (titleRes != other.titleRes) return false
        if (summary != other.summary) return false
        if (summaryRes != other.summaryRes) return false
        if (icon != other.icon) return false
        if (iconRes != other.iconRes) return false
        if (preserveIconSpace != other.preserveIconSpace) return false
        if (defaultValue != other.defaultValue) return false
        if (enabled != other.enabled) return false
        if (isClickable != other.isClickable) return false
        if (dependencies != other.dependencies) return false
        if (persistent != other.persistent) return false
        if (layoutRes != other.layoutRes) return false
        if (widgetLayoutRes != other.widgetLayoutRes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + titleRes
        result = 31 * result + (summary?.hashCode() ?: 0)
        result = 31 * result + summaryRes
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + iconRes
        result = 31 * result + preserveIconSpace.hashCode()
        result = 31 * result + (defaultValue?.hashCode() ?: 0)
        result = 31 * result + enabled.hashCode()
        result = 31 * result + isClickable.hashCode()
        result = 31 * result + dependencies.hashCode()
        result = 31 * result + persistent.hashCode()
        result = 31 * result + layoutRes
        result = 31 * result + widgetLayoutRes
        return result
    }

    /**
     * A [EpoxyHolder] for [AbstractPreferenceModel]'s
     */
    open class Holder : EpoxyHolder(), LayoutContainer {

        override lateinit var containerView: View

        @CallSuper
        override fun bindView(view: View) {
            containerView = view
        }
    }

    abstract class Builder<T : Any> {

        var key: String by Delegates.notNull()
            private set
        var title: String? = null
            private set
        var titleRes: Int = 0
            private set
        var summary: String? = null
            private set
        var summaryRes: Int = 0
            private set
        var icon: Drawable? = null
            private set
        var iconRes: Int = 0
            private set
        var preserveIconSpace: Boolean = false
            private set
        var defaultValue: T? = null
            private set
        var isEnabled: Boolean = true
            private set
        var isClickable: Boolean = true
            private set
        val dependencies = mutableListOf<PreferenceDependency<*>>()
        var onClick: (() -> Boolean)? = null
            private set
        var onChange: ((newValue: T) -> Boolean)? = null
            private set
        var isPersistent: Boolean = true
            private set
        var layoutRes: Int = R.layout.item_preference
            private set
        var widgetLayoutRes: Int = 0
            private set
        var context: PreferenceContext? = null
            private set

        fun key(key: String) {
            this.key = key
        }

        fun title(title: String?) {
            this.title = title
        }

        fun titleRes(titleRes: Int) {
            this.titleRes = titleRes
        }

        fun summary(summary: String?) {
            this.summary = summary
        }

        fun summaryRes(summaryRes: Int) {
            this.summaryRes = summaryRes
        }

        fun icon(icon: Drawable?) {
            this.icon = icon
        }

        fun iconRes(iconRes: Int) {
            this.iconRes = iconRes
        }

        fun preserveIconSpace(preserveIconSpace: Boolean) {
            this.preserveIconSpace = preserveIconSpace
        }

        fun defaultValue(defaultValue: T?) {
            this.defaultValue = defaultValue
        }

        fun isEnabled(isEnabled: Boolean) {
            this.isEnabled = isEnabled
        }

        fun isClickable(isClickable: Boolean) {
            this.isClickable = isClickable
        }

        fun dependencies(vararg dependencies: PreferenceDependency<*>) {
            this.dependencies.addAll(dependencies)
        }

        fun dependencies(dependencies: Iterable<PreferenceDependency<*>>) {
            this.dependencies.addAll(dependencies)
        }

        fun onClick(onClick: (() -> Boolean)?) {
            this.onClick = onClick
        }

        fun onChange(onChange: ((T) -> Boolean)?) {
            this.onChange = onChange
        }

        fun isPersistent(isPersistent: Boolean) {
            this.isPersistent = isPersistent
        }

        fun context(context: PreferenceContext?) {
            this.context = context
        }

        fun layoutRes(layoutRes: Int) {
            this.layoutRes = layoutRes
        }

        fun widgetLayoutRes(widgetLayoutRes: Int) {
            this.widgetLayoutRes = widgetLayoutRes
        }

        abstract fun build(): AbstractPreferenceModel<T>
    }

}

fun <T : Any> AbstractPreferenceModel.Builder<*>.dependency(
    key: String,
    value: T,
    defaultValue: T? = value.tryToResolveDefaultValue() as? T
) {
    dependencies(PreferenceDependency(key, value, defaultValue))
}