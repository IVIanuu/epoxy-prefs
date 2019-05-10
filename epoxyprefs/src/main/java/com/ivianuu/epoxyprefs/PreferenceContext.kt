package com.ivianuu.epoxyprefs

import android.annotation.SuppressLint
import android.content.SharedPreferences

interface PreferenceContext {
    fun <T : Any> get(key: String): T?
    fun <T : Any> getOrDefault(key: String, defaultValue: T): T =
        get(key) ?: defaultValue

    fun <T : Any> set(key: String, value: T)
}

fun PreferenceContext(
    sharedPreferences: SharedPreferences,
    useCommit: Boolean
): PreferenceContext = DefaultPreferenceContext(sharedPreferences, useCommit)

private class DefaultPreferenceContext(
    val sharedPreferences: SharedPreferences,
    val useCommit: Boolean
) : PreferenceContext {

    override fun <T : Any> get(key: String): T? =
        sharedPreferences.all[key] as? T

    @SuppressLint("ApplySharedPref")
    override fun <T : Any> set(key: String, value: T) {
        sharedPreferences.edit().apply {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Set<*> -> putStringSet(key, value as Set<String>)
            }

            if (useCommit) {
                commit()
            } else {
                apply()
            }
        }
    }
}