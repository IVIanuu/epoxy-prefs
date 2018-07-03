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
import com.airbnb.epoxy.EpoxyAttribute

/**
 * A list preference model
 */
abstract class ListPreferenceModel(
    context: Context
) : DialogPreferenceModel(context) {

    @EpoxyAttribute var entries: Array<CharSequence>? = null
    @EpoxyAttribute var entryValues: Array<CharSequence>? = null

    fun entries(entries: Array<CharSequence>?) {
        this.entries = entries
    }

    fun entryValues(entryValues: Array<CharSequence>?) {
        this.entryValues = entryValues
    }

}

fun ListPreferenceModel.entriesRes(entriesRes: Int) {
    entries(context.resources.getTextArray(entriesRes))
}

fun ListPreferenceModel.entryValuesRes(entryValuesRes: Int) {
    entryValues(context.resources.getTextArray(entryValuesRes))
}