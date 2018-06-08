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
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_preference.*

/**
 * Base preference
 */
@EpoxyModelClass
abstract class PreferenceModel(
    val context: Context
) : EpoxyModelWithHolder<PreferenceModel.Holder>() {

    @EpoxyAttribute var key: String? = null
        set(value) {
            field = value
            id("pref_$value")
        }
    @EpoxyAttribute var title: CharSequence? = null
    @EpoxyAttribute var summary: CharSequence? = null
    @EpoxyAttribute var icon: Drawable? = null

    @EpoxyAttribute var defaultValue: Any? = null

    @EpoxyAttribute var visible: Boolean = true
    @EpoxyAttribute var enabled: Boolean = true

    @EpoxyAttribute var dependencyKey: String? = null
    @EpoxyAttribute var dependencyValue: Any? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var clickListener: ClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var changeListener: ChangeListener? = null

    @EpoxyAttribute var sharedPreferencesName: String? = null

    @EpoxyAttribute var useCommit: Boolean = EpoxyPrefs.getUseCommit()

    @EpoxyAttribute var layoutRes: Int = R.layout.item_preference
        set(value) {
            field = value
            layout(field + widgetLayoutRes)
        }
    @EpoxyAttribute var widgetLayoutRes: Int = 0
        set(value) {
            field = value
            layout(layoutRes + field)
        }

    protected val sharedPreferences: SharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        val sharedPreferencesName = sharedPreferencesName
                ?: EpoxyPrefs.getDefaultSharedPreferencesName(context)
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

    protected fun getPersistedBoolean(
        key: String,
        fallback: Boolean = defaultValue as? Boolean? ?: false
    ): Boolean {
        return sharedPreferences.getBoolean(key, fallback)
    }

    protected fun getPersistedFloat(
        key: String,
        fallback: Float = defaultValue as? Float? ?: 0f
    ): Float {
        return sharedPreferences.getFloat(key, fallback)
    }

    protected fun getPersistedInt(key: String, fallback: Int = defaultValue as? Int? ?: 0): Int {
        return sharedPreferences.getInt(key, fallback)
    }

    protected fun getPersistedLong(
        key: String,
        fallback: Long = defaultValue as? Long? ?: 0L
    ): Long {
        return sharedPreferences.getLong(key, fallback)
    }

    protected fun getPersistedString(
        key: String,
        fallback: String = defaultValue as? String? ?: ""
    ): String {
        return sharedPreferences.getString(key, fallback)
    }

    protected fun getPersistedStringSet(
        key: String,
        fallback: MutableSet<String> = defaultValue as? MutableSet<String>? ?: mutableSetOf()
    ): MutableSet<String> {
        return sharedPreferences.getStringSet(key, fallback)
    }

    @SuppressLint("ApplySharedPref")
    protected fun editSharedPreferences(edit: SharedPreferences.Editor.() -> Unit) {
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

    protected fun persistBoolean(key: String, value: Boolean) {
        editSharedPreferences { putBoolean(key, value) }
    }

    protected fun persistFloat(key: String, value: Float) {
        editSharedPreferences { putFloat(key, value) }
    }

    protected fun persistInt(key: String, value: Int) {
        editSharedPreferences { putInt(key, value) }
    }

    protected fun persistLong(key: String, value: Long) {
        editSharedPreferences { putLong(key, value) }
    }

    protected fun persistString(key: String, value: String) {
        editSharedPreferences { putString(key, value) }
    }

    protected fun persistStringSet(key: String, value: MutableSet<String>) {
        editSharedPreferences { putStringSet(key, value) }
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
}