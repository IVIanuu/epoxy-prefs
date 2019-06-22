package com.ivianuu.epoxyprefs

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal fun Any?.tryToResolveDefaultValue(): Any? = when (this) {
    is Boolean -> false
    is Float -> 0f
    is Int -> 0
    is Long -> 0L
    is String -> ""
    is Set<*> -> emptySet<String>()
    else -> null
}

internal fun <T : AbstractPreferenceModel.Builder<*>> T.injectContextIfPossible(
    controller: EpoxyController
): T {
    (controller as? PreferenceEpoxyController)?.let { context(it.context) }
    return this
}

internal fun resolveBoolean(
    context: Context,
    attr: Int,
    defaultValue: Boolean
): Boolean {
    val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getBoolean(0, defaultValue)
    } finally {
        a.recycle()
    }
}

internal fun <T> lazyVar(block: () -> T) = object : ReadWriteProperty<Any, T> {

    private var _value: Any? = this

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (_value === this) {
            _value = block()
        }

        return _value as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        _value = value
    }
}