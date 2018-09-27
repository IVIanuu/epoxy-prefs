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
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ivianuu.epoxyprefs.internal.setTextFuture
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_preference.*
import java.util.*

/**
 * Base preference
 */
open class PreferenceModel(builder: Builder) : EpoxyModelWithHolder<PreferenceModel.Holder>() {

    val context = builder.context
    val key = builder.key
    val title = builder.title
    val summary = builder.summary
    val icon = builder.icon
    val defaultValue = builder.defaultValue
    val enabled = builder.enabled
    val clickable = builder.clickable
    val dependencyKey = builder.dependencyKey
    val dependencyValue = builder.dependencyValue
    val allowedByDependency = builder.allowedByDependency
    val clickListener = builder.clickListener
    val changeListener = builder.changeListener
    val sharedPreferences = builder.realSharedPreferences
    val sharedPreferencesName = builder.sharedPreferencesName
    val useCommit = builder.useCommit
    val persistent = builder.persistent
    val layoutRes = builder.layoutRes
    val widgetLayoutRes = builder.widgetLayoutRes
    val value = builder.value

    init {
        id(key ?: UUID.randomUUID().toString()) // todo remove this
        layout(layoutRes + widgetLayoutRes)
    }

    @CallSuper
    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.title?.let {
            if (title != null) it.setTextFuture(title)
            it.text = title
            it.visibility = if (title != null) View.VISIBLE else View.GONE
        }

        holder.summary?.let {
            if (summary != null) it.setTextFuture(summary)
            it.visibility = if (summary != null) View.VISIBLE else View.GONE
        }

        holder.icon?.let {
            it.setImageDrawable(icon)
        }

        holder.icon_frame?.let {
            it.visibility = if (icon != null) View.VISIBLE else View.GONE
        }

        holder.containerView.run {
            val enabled = enabled && allowedByDependency
            isEnabled = enabled
            alpha = if (enabled) 1f else 0.5f

            isClickable = clickable

            if (clickable) {
                setOnClickListener {
                    val handled =
                        clickListener?.invoke(this@PreferenceModel) ?: false
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
            holder.containerView.setOnClickListener(null)
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

    protected fun callChangeListener(newValue: Any) =
        changeListener?.invoke(this, newValue) ?: true

    protected fun shouldPersist() = persistent && key != null

    @SuppressLint("ApplySharedPref")
    protected fun editSharedPreferences(edit: SharedPreferences.Editor.() -> Unit) {
        if (shouldPersist()) {
            sharedPreferences.edit()
                .also(edit)
                .run {
                    if (useCommit) {
                        commit()
                    } else {
                        apply()
                    }
                }
        }
    }

    protected fun persistBoolean(key: String?, value: Boolean) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putBoolean(key, value) }
        }
    }

    protected fun persistFloat(key: String?, value: Float) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putFloat(key, value) }
        }
    }

    protected fun persistInt(key: String?, value: Int) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putInt(key, value) }
        }
    }

    protected fun persistLong(key: String?, value: Long) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putLong(key, value) }
        }
    }

    protected fun persistString(key: String?, value: String) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putString(key, value) }
        }
    }

    protected fun persistStringSet(key: String?, value: MutableSet<String>) {
        if (shouldPersist()) {
            if (key == null) throw IllegalArgumentException("key == null")
            editSharedPreferences { putStringSet(key, value) }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as PreferenceModel

        if (key != other.key) return false
        if (title != other.title) return false
        if (summary != other.summary) return false
        if (icon != other.icon) return false
        if (defaultValue != other.defaultValue) return false
        if (enabled != other.enabled) return false
        if (clickable != other.clickable) return false
        if (dependencyKey != other.dependencyKey) return false
        if (dependencyValue != other.dependencyValue) return false
        if (allowedByDependency != other.allowedByDependency) return false
        if (sharedPreferencesName != other.sharedPreferencesName) return false
        if (useCommit != other.useCommit) return false
        if (persistent != other.persistent) return false
        if (layoutRes != other.layoutRes) return false
        if (widgetLayoutRes != other.widgetLayoutRes) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (key?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (summary?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (defaultValue?.hashCode() ?: 0)
        result = 31 * result + enabled.hashCode()
        result = 31 * result + clickable.hashCode()
        result = 31 * result + (dependencyKey?.hashCode() ?: 0)
        result = 31 * result + (dependencyValue?.hashCode() ?: 0)
        result = 31 * result + allowedByDependency.hashCode()
        result = 31 * result + (sharedPreferencesName?.hashCode() ?: 0)
        result = 31 * result + useCommit.hashCode()
        result = 31 * result + persistent.hashCode()
        result = 31 * result + layoutRes
        result = 31 * result + widgetLayoutRes
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }

    /**
     * A [EpoxyHolder] for [PreferenceModel]'s
     */
    open class Holder : EpoxyHolder(), LayoutContainer {

        override lateinit var containerView: View

        @CallSuper
        override fun bindView(view: View) {
            containerView = view
        }
    }

    open class Builder(val context: Context) {

        var key: String? = null
            private set
        var title: CharSequence? = null
            private set
        var summary: CharSequence? = null
            private set
        var icon: Drawable? = null
            private set
        var defaultValue: Any? = null
            private set
        var enabled: Boolean = true
            private set
        var clickable: Boolean = true
            private set
        var dependencyKey: String? = null
            private set
        var dependencyValue: Any? = null
            private set
        val allowedByDependency
            get() = if (dependencyKey != null && dependencyValue != null) {
                realSharedPreferences.all[dependencyKey] == dependencyValue
            } else {
                true
            }
        var clickListener: ((preference: PreferenceModel) -> Boolean)? = null
            private set
        var changeListener: ((preference: PreferenceModel, newValue: Any) -> Boolean)? = null
            private set
        var sharedPreferences: SharedPreferences? = null
            private set
        var sharedPreferencesName: String? = null
            private set
        var useCommit: Boolean = EpoxyPrefsPlugins.getUseCommit()
            private set
        var persistent: Boolean = true
            private set
        var layoutRes: Int = R.layout.item_preference
            private set
        var widgetLayoutRes: Int = 0
            private set
        internal val value
            get() = if (key != null && persistent) {
                realSharedPreferences.all[key] ?: defaultValue
            } else if (!persistent) {
                defaultValue
            } else {
                null
            }


        internal val realSharedPreferences
            get() = if (sharedPreferences != null) {
                sharedPreferences!!
            } else {
                if (sharedPreferencesName != null) {
                    context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                } else {
                    EpoxyPrefsPlugins.getDefaultSharedPreferences(context)
                }
            }

        fun key(key: String) {
            this.key = key
        }

        fun title(title: CharSequence?) {
            this.title = title
        }

        fun summary(summary: CharSequence?) {
            this.summary = summary
        }

        fun icon(icon: Drawable?) {
            this.icon = icon
        }

        fun defaultValue(defaultValue: Any?) {
            this.defaultValue = defaultValue
        }

        fun enabled(enabled: Boolean) {
            this.enabled = enabled
        }

        fun clickable(clickable: Boolean) {
            this.clickable = clickable
        }

        fun dependencyKey(dependencyKey: String?) {
            this.dependencyKey = dependencyKey
        }

        fun dependencyValue(dependencyValue: Any?) {
            this.dependencyValue = dependencyValue
        }

        fun clickListener(clickListener: (preference: PreferenceModel) -> Boolean) {
            this.clickListener = clickListener
        }

        fun changeListener(changeListener: (preference: PreferenceModel, newValue: Any) -> Boolean) {
            this.changeListener = changeListener
        }

        fun sharedPreferences(sharedPreferences: SharedPreferences?) {
            this.sharedPreferences = sharedPreferences
        }

        fun sharedPreferencesName(sharedPreferencesName: String?) {
            this.sharedPreferencesName = sharedPreferencesName
        }

        fun useCommit(useCommit: Boolean) {
            this.useCommit = useCommit
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

        open fun build() = PreferenceModel(this)
    }
}

inline fun EpoxyController.preference(context: Context, init: PreferenceModel.Builder.() -> Unit) =
    PreferenceModel.Builder(context)
        .apply(init)
        .build()
        .also { it.addTo(this) }

inline fun PreferenceEpoxyController.preference(init: PreferenceModel.Builder.() -> Unit) =
    preference(context, init)

fun PreferenceModel.Builder.titleRes(titleRes: Int) {
    title(context.getString(titleRes))
}

fun PreferenceModel.Builder.summaryRes(summaryRes: Int) {
    summary(context.getString(summaryRes))
}

fun PreferenceModel.Builder.icon(iconRes: Int) {
    icon(ContextCompat.getDrawable(context, iconRes))
}

fun PreferenceModel.Builder.dependency(key: String?, value: Any?) {
    dependencyKey(key)
    dependencyValue(value)
}

@JvmName("changeListenerTyped")
inline fun <reified T> PreferenceModel.Builder.changeListener(crossinline changeListener: (preference: PreferenceModel, newValue: T) -> Boolean) {
    changeListener { preference: PreferenceModel, newValue: Any ->
        changeListener(
            preference,
            newValue as T
        )
    }
}

fun PreferenceModel.Builder.intentClickListener(intent: (PreferenceModel) -> Intent) =
    clickListener {
        it.context.startActivity(intent(it))
    true
}

fun PreferenceModel.Builder.urlClickListener(url: (PreferenceModel) -> String) =
    intentClickListener {
        Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url(it)) }
}