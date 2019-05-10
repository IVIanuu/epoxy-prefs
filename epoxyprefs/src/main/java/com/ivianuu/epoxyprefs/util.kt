package com.ivianuu.epoxyprefs

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

private object UNINITIALIZED

internal fun <T> lazyVar(block: () -> T) = object : ReadWriteProperty<Any, T> {

    private var _value: Any? = UNINITIALIZED

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (_value === UNINITIALIZED) {
            _value = block()
        }

        return _value as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        _value = value
    }
}