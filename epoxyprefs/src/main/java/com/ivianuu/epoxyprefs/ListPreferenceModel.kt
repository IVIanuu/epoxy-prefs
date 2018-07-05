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

/**
 * A list preference model
 */
abstract class ListPreferenceModel(builder: Builder) : DialogPreferenceModel(builder) {

    open val entries = builder.entries
    open val entryValues = builder.entryValues

    abstract class Builder(context: Context) : DialogPreferenceModel.Builder(context) {

        open var entries: Array<CharSequence>? = null
        open var entryValues: Array<CharSequence>? = null

        open fun entries(entries: Array<CharSequence>?) {
            this.entries = entries
        }

        open fun entryValues(entryValues: Array<CharSequence>?) {
            this.entryValues = entryValues
        }

    }
}

fun ListPreferenceModel.Builder.entriesRes(entriesRes: Int) {
    entries(context.resources.getTextArray(entriesRes))
}

fun ListPreferenceModel.Builder.entryValuesRes(entryValuesRes: Int) {
    entryValues(context.resources.getTextArray(entryValuesRes))
}