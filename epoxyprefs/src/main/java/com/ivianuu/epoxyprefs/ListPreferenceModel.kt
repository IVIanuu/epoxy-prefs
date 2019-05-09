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

import android.content.Context
import java.util.*

/**
 * A list preference model
 */
abstract class ListPreferenceModel<T : Any>(builder: Builder<T>) :
    DialogPreferenceModel<T>(builder) {

    val entries = builder.entries
    val entryValues = builder.entryValues

    abstract class Builder<T : Any>(context: Context) : DialogPreferenceModel.Builder<T>(context) {

        var entries: Array<String>? = null
        var entryValues: Array<String>? = null

        open fun entries(entries: Array<String>?) {
            this.entries = entries
        }

        open fun entryValues(entryValues: Array<String>?) {
            this.entryValues = entryValues
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ListPreferenceModel<*>) return false
        if (!super.equals(other)) return false

        if (!Arrays.equals(entries, other.entries)) return false
        if (!Arrays.equals(entryValues, other.entryValues)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (entries?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + (entryValues?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}

fun ListPreferenceModel.Builder<*>.entries(entriesRes: Int) {
    entries(context.resources.getStringArray(entriesRes))
}

fun ListPreferenceModel.Builder<*>.entryValues(entryValuesRes: Int) {
    entryValues(context.resources.getStringArray(entryValuesRes))
}