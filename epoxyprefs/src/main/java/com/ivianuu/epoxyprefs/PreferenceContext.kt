package com.ivianuu.epoxyprefs

import android.annotation.SuppressLint
import android.content.SharedPreferences

/**
 * @author Manuel Wrage (IVIanuu)
 */
data class PreferenceContext(
    val sharedPreferences: SharedPreferences,
    val useCommit: Boolean
) {

    fun <T : Any> get(key: String, defaultValue: T? = null): T? =
        sharedPreferences.all[key] as? T ?: defaultValue

    fun <T : Any> getOrDefault(key: String, defaultValue: T): T =
        get(key) ?: defaultValue

    @SuppressLint("ApplySharedPref")
    fun <T : Any> set(key: String, value: T) {
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