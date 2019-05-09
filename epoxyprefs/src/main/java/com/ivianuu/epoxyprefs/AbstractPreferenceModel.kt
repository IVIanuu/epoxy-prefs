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
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_preference.*

/**
 * Base preference
 */
open class AbstractPreferenceModel<T : Any>(
    builder: Builder<T>
) : EpoxyModelWithHolder<AbstractPreferenceModel.Holder>() {

    val context = builder.context
    val key = builder.key ?: throw IllegalStateException("missing key")
    val title = builder.title
    val summary = builder.summary
    val icon = builder.icon
    val preserveIconSpace = builder.preserveIconSpace
    val defaultValue = builder.defaultValue
    val enabled = builder.enabled
    val clickable = builder.clickable
    val dependencies = builder.dependencies
    val allowedByDependencies = builder.allowedByDependencies
    val onClick = builder.onClick
    val onChange = builder.onChange
    val persistent = builder.persistent
    val layoutRes = builder.layoutRes
    val widgetLayoutRes = builder.widgetLayoutRes
    val value = builder.value

    protected val viewsShouldBeEnabled: Boolean get() = enabled && allowedByDependencies

    init {
        id(key)
        layout(layoutRes + widgetLayoutRes)
    }

    @CallSuper
    override fun bind(holder: Holder) {
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
            isClickable = clickable

            if (clickable) {
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

    @CallSuper
    override fun unbind(holder: Holder) {
        super.unbind(holder)

        with(holder) {
            title?.text = null
            summary?.text = null
            icon?.setImageDrawable(null)
            icon_frame?.visibility = View.VISIBLE
            containerView.isEnabled = true
            containerView.alpha = 1f
            containerView.isClickable = true
            containerView.setOnClickListener(null)
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

        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AbstractPreferenceModel<*>

        if (key != other.key) return false
        if (title != other.title) return false
        if (summary != other.summary) return false
        if (icon != other.icon) return false
        if (preserveIconSpace != other.preserveIconSpace) return false
        if (defaultValue != other.defaultValue) return false
        if (enabled != other.enabled) return false
        if (clickable != other.clickable) return false
        if (dependencies != other.dependencies) return false
        if (allowedByDependencies != other.allowedByDependencies) return false
        if (persistent != other.persistent) return false
        if (layoutRes != other.layoutRes) return false
        if (widgetLayoutRes != other.widgetLayoutRes) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (summary?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + preserveIconSpace.hashCode()
        result = 31 * result + (defaultValue?.hashCode() ?: 0)
        result = 31 * result + enabled.hashCode()
        result = 31 * result + clickable.hashCode()
        result = 31 * result + dependencies.hashCode()
        result = 31 * result + allowedByDependencies.hashCode()
        result = 31 * result + persistent.hashCode()
        result = 31 * result + layoutRes
        result = 31 * result + widgetLayoutRes
        result = 31 * result + (value?.hashCode() ?: 0)
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

    class Dependency(
        val key: String,
        val value: Any?,
        val defaultValue: Any?
    ) {
        fun isOk(context: PreferenceContext): Boolean {
            return (context.get(key, defaultValue) ?: value.tryToResolveDefaultValue()) == value
        }
    }

    abstract class Builder<T : Any>(val context: Context) {

        var preferenceContext: PreferenceContext = EpoxyPrefsPlugins.getDefaultContext(context)
            private set
        var key: String? = null
            private set
        var title: String? = null
            private set
        var summary: String? = null
            private set
        var icon: Drawable? = null
            private set
        var preserveIconSpace: Boolean = false
            private set
        var defaultValue: T? = null
            private set
        var enabled: Boolean = true
            private set
        var clickable: Boolean = true
            private set
        val dependencies = mutableListOf<Dependency>()
        val allowedByDependencies: Boolean
            get() = dependencies.all { it.isOk(preferenceContext) }
        var onClick: (() -> Boolean)? = null
            private set
        var onChange: ((newValue: T) -> Boolean)? = null
            private set
        var persistent: Boolean = true
            private set
        var layoutRes: Int = R.layout.item_preference
            private set
        var widgetLayoutRes: Int = 0
            private set
        internal val value: T?
            get() = if (key != null && persistent) {
                preferenceContext.get(key!!, defaultValue)
            } else if (!persistent) {
                defaultValue
            } else {
                null
            }

        fun key(key: String) {
            this.key = key
        }

        fun title(title: String?) {
            this.title = title
        }

        fun summary(summary: String?) {
            this.summary = summary
        }

        fun icon(icon: Drawable?) {
            this.icon = icon
        }

        fun preserveIconSpace(preserveIconSpace: Boolean) {
            this.preserveIconSpace = preserveIconSpace
        }

        fun defaultValue(defaultValue: T?) {
            this.defaultValue = defaultValue
        }

        fun enabled(enabled: Boolean) {
            this.enabled = enabled
        }

        fun clickable(clickable: Boolean) {
            this.clickable = clickable
        }

        fun dependency(dependency: Dependency) {
            dependencies.add(dependency)
        }

        fun onClick(onClick: () -> Boolean) {
            this.onClick = onClick
        }

        fun onChange(onChange: (newValue: T) -> Boolean) {
            this.onChange = onChange
        }

        fun persistent(persistent: Boolean) {
            this.persistent = persistent
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

fun AbstractPreferenceModel.Builder<*>.title(titleRes: Int) {
    title(context.getString(titleRes))
}

fun AbstractPreferenceModel.Builder<*>.summary(summaryRes: Int) {
    summary(context.getString(summaryRes))
}

fun AbstractPreferenceModel.Builder<*>.icon(iconRes: Int) {
    icon(ContextCompat.getDrawable(context, iconRes))
}

fun AbstractPreferenceModel.Builder<*>.dependency(
    key: String,
    value: Any?,
    defaultValue: Any? = null
) {
    dependency(AbstractPreferenceModel.Dependency(key, value, defaultValue))
}

fun AbstractPreferenceModel.Builder<*>.onClickIntent(intent: () -> Intent) {
    onClick {
        context.startActivity(intent())
        return@onClick true
    }
}

fun AbstractPreferenceModel.Builder<*>.onClickUrl(url: () -> String) {
    onClickIntent {
        Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url()) }
    }
}