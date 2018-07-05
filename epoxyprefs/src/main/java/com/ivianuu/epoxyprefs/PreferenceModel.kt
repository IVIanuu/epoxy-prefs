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
import android.support.annotation.CallSuper
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ivianuu.epoxyprefs.util.IntentPreferenceModelClickListener
import com.ivianuu.epoxyprefs.util.UrlPreferenceModelClickListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_preference.*
import java.util.*

/**
 * Base preference
 */
open class PreferenceModel(builder: Builder) : EpoxyModelWithHolder<PreferenceModel.Holder>() {

    open val context = builder.context
    open val key = builder.key
    open val title = builder.title
    open val summary = builder.summary
    open val icon = builder.icon
    open val defaultValue = builder.defaultValue
    open val enabled = builder.enabled
    open val dependencyKey = builder.dependencyKey
    open val dependencyValue = builder.dependencyValue
    open val clickListener = builder.clickListener
    open val changeListener = builder.changeListener
    open val sharedPreferencesName = builder.sharedPreferencesName
    open val useCommit = builder.useCommit
    open val persistent = builder.persistent
    open val layoutRes = builder.layoutRes
    open val widgetLayoutRes = builder.widgetLayoutRes

    protected val sharedPreferences: SharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        val sharedPreferencesName = sharedPreferencesName
                ?: EpoxyPrefsPlugins.getDefaultSharedPreferencesName(context)
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    protected var currentHolder: Holder? = null

    private val sharedPreferencesChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (this.key == key) {
                onChange()
            } else if (this.dependencyKey == key) {
                onDependencyChange()
            }
        }

    private var listeningForChanges = false

    init {
        id(key ?: UUID.randomUUID().toString())
        layout(layoutRes + widgetLayoutRes)
    }

    @CallSuper
    override fun bind(holder: Holder) {
        super.bind(holder)

        currentHolder = holder

        holder.title?.let {
            it.text = title
            it.visibility = if (title != null) View.VISIBLE else View.GONE
        }

        holder.summary?.let {
            it.text = summary
            it.visibility = if (summary != null) View.VISIBLE else View.GONE
        }

        holder.icon?.let {
            it.setImageDrawable(icon)
        }

        holder.icon_frame?.let {
            it.visibility = if (icon != null) View.VISIBLE else View.GONE
        }

        val dependencyKey = dependencyKey
        val dependencyValue = dependencyValue

        val matchesDependency = if (dependencyKey != null && dependencyValue != null) {
            val currentDependencyValue = sharedPreferences.all[dependencyKey]
            dependencyValue == currentDependencyValue
        } else {
            true
        }

        val enabled = enabled && matchesDependency

        holder.containerView.isEnabled = enabled
        holder.containerView.alpha = if (enabled) 1f else 0.5f

        holder.containerView.setOnClickListener {
            val handled = clickListener?.onPreferenceClicked(this) ?: false
            if (!handled) {
                onClick()
            }
        }

        startListeningForChanges()
    }

    @CallSuper
    override fun unbind(holder: Holder) {
        super.unbind(holder)
        stopListeningForChanges()

        with(holder) {
            title?.text = null
            summary?.text = null
            icon?.setImageDrawable(null)
            holder.containerView.setOnClickListener(null)
        }

        currentHolder = null
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

    protected open fun onChange() {
    }

    protected open fun onDependencyChange() {
        val dependencyKey = dependencyKey ?: return
        val dependencyValue = dependencyValue ?: return
        val currentHolder = currentHolder ?: return

        val newDependencyValue = sharedPreferences.all[dependencyKey]

        val enabled = enabled && dependencyValue == newDependencyValue

        currentHolder.containerView.isEnabled = enabled
        currentHolder.containerView.alpha = if (enabled) 1f else 0.5f
    }

    protected fun callChangeListener(newValue: Any): Boolean {
        return changeListener?.onPreferenceChange(this, newValue) ?: true
    }

    protected fun shouldPersist(): Boolean {
        return persistent && key != null
    }

    protected fun getPersistedBoolean(
        key: String?,
        fallback: Boolean = defaultValue as? Boolean? ?: false
    ): Boolean {
        return if (shouldPersist()) {
            sharedPreferences.getBoolean(key, fallback)
        } else {
            fallback
        }
    }

    protected fun getPersistedFloat(
        key: String?,
        fallback: Float = defaultValue as? Float? ?: 0f
    ): Float {
        return if (shouldPersist()) {
            sharedPreferences.getFloat(key, fallback)
        } else {
            fallback
        }
    }

    protected fun getPersistedInt(key: String?, fallback: Int = defaultValue as? Int? ?: 0): Int {
        return if (shouldPersist()) {
            sharedPreferences.getInt(key, fallback)
        } else {
            fallback
        }
    }

    protected fun getPersistedLong(
        key: String?,
        fallback: Long = defaultValue as? Long? ?: 0L
    ): Long {
        return if (shouldPersist()) {
            sharedPreferences.getLong(key, fallback)
        } else {
            fallback
        }
    }

    protected fun getPersistedString(
        key: String?,
        fallback: String = defaultValue as? String? ?: ""
    ): String {
        return if (shouldPersist()) {
            sharedPreferences.getString(key, fallback)
        } else {
            fallback
        }
    }

    protected fun getPersistedStringSet(
        key: String?,
        fallback: MutableSet<String> = defaultValue as? MutableSet<String>? ?: mutableSetOf()
    ): MutableSet<String> {
        return if (shouldPersist()) {
            sharedPreferences.getStringSet(key, fallback)
        } else {
            fallback
        }
    }

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

    private fun startListeningForChanges() {
        if (!listeningForChanges) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(
                sharedPreferencesChangeListener
            )
            listeningForChanges = true
        }
    }

    private fun stopListeningForChanges() {
        if (listeningForChanges) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(
                sharedPreferencesChangeListener
            )
            listeningForChanges = false
        }
    }

    /**
     * A click listener for [PreferenceModel]'s
     */
    interface ClickListener {
        fun onPreferenceClicked(preference: PreferenceModel): Boolean
    }

    /**
     * A change listener for [PreferenceModel]'s
     */
    interface ChangeListener {
        fun onPreferenceChange(preference: PreferenceModel, newValue: Any): Boolean
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

        open var key: String? = null
        open var title: CharSequence? = null
        open var summary: CharSequence? = null
        open var icon: Drawable? = null
        open var defaultValue: Any? = null
        open var enabled: Boolean = true
        open var dependencyKey: String? = null
        open var dependencyValue: Any? = null
        open var clickListener: ClickListener? = null
        open var changeListener: ChangeListener? = null
        open var sharedPreferencesName: String? = null
        open var useCommit: Boolean = EpoxyPrefsPlugins.getUseCommit()
        open var persistent: Boolean = true
        open var layoutRes: Int = R.layout.item_preference
        open var widgetLayoutRes: Int = 0

        open fun key(key: String) {
            this.key = key
        }

        open fun title(title: CharSequence?) {
            this.title = title
        }

        open fun summary(summary: CharSequence?) {
            this.summary = summary
        }

        open fun icon(icon: Drawable?) {
            this.icon = icon
        }

        open fun defaultValue(defaultValue: Any?) {
            this.defaultValue = defaultValue
        }

        open fun enabled(enabled: Boolean) {
            this.enabled = enabled
        }

        open fun dependencyKey(dependencyKey: String?) {
            this.dependencyKey = dependencyKey
        }

        open fun dependencyValue(dependencyValue: Any?) {
            this.dependencyValue = dependencyValue
        }

        open fun clickListener(clickListener: PreferenceModel.ClickListener?) {
            this.clickListener = clickListener
        }

        open fun changeListener(changeListener: PreferenceModel.ChangeListener?) {
            this.changeListener = changeListener
        }

        open fun sharedPreferencesName(sharedPreferencesName: String?) {
            this.sharedPreferencesName = sharedPreferencesName
        }

        open fun useCommit(useCommit: Boolean) {
            this.useCommit = useCommit
        }

        open fun persistent(persistent: Boolean) {
            this.persistent = persistent
        }

        open fun layoutRes(layoutRes: Int) {
            this.layoutRes = layoutRes
        }

        open fun widgetLayoutRes(widgetLayoutRes: Int) {
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

fun PreferenceModel.Builder.iconRes(iconRes: Int) {
    icon(ContextCompat.getDrawable(context, iconRes))
}

fun PreferenceModel.Builder.dependency(key: String?, value: Any?) {
    dependencyKey(key)
    dependencyValue(value)
}

fun PreferenceModel.Builder.clickListener(clickListener: (preference: PreferenceModel) -> Boolean) {
    clickListener(object : PreferenceModel.ClickListener {
        override fun onPreferenceClicked(preference: PreferenceModel): Boolean {
            return clickListener.invoke(preference)
        }
    })
}

fun PreferenceModel.Builder.changeListener(changeListener: (preference: PreferenceModel, newValue: Any) -> Boolean) {
    changeListener(object : PreferenceModel.ChangeListener {
        override fun onPreferenceChange(preference: PreferenceModel, newValue: Any): Boolean {
            return changeListener.invoke(preference, newValue)
        }
    })
}

fun PreferenceModel.Builder.intentClickListener(intent: Intent) {
    clickListener(IntentPreferenceModelClickListener(intent))
}

fun PreferenceModel.Builder.intentClickListener(intent: (preference: PreferenceModel) -> Intent?) {
    clickListener(IntentPreferenceModelClickListener(intent))
}

fun PreferenceModel.Builder.urlClickListener(url: String) {
    clickListener(UrlPreferenceModelClickListener(url))
}

fun PreferenceModel.Builder.urlClickListener(url: (preference: PreferenceModel) -> String?) {
    clickListener(UrlPreferenceModelClickListener(url))
}