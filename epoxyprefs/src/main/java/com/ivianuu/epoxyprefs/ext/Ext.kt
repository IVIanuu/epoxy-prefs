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

package com.ivianuu.epoxyprefs.ext

import android.content.Intent
import android.support.v4.content.ContextCompat
import com.ivianuu.epoxyprefs.PreferenceModel
import com.ivianuu.epoxyprefs.PreferenceModelBuilder_
import com.ivianuu.epoxyprefs.util.IntentPreferenceModelClickListener
import com.ivianuu.epoxyprefs.util.UrlPreferenceModelClickListener

private val <T : PreferenceModelBuilder_> T.context get() = model.context

fun <T : PreferenceModelBuilder_> T.titleRes(titleRes: Int): T {
    title(context.getString(titleRes))
    return this
}

fun <T : PreferenceModelBuilder_> T.summaryRes(summaryRes: Int): T {
    summary(context.getString(summaryRes))
    return this
}

fun <T : PreferenceModelBuilder_> T.iconRes(iconRes: Int): T {
    icon(ContextCompat.getDrawable(context, iconRes))
    return this
}

fun <T : PreferenceModelBuilder_> T.dependency(key: String?, value: Any?): T {
    dependencyKey(key)
    dependencyValue(value)
    return this
}

fun <T : PreferenceModelBuilder_> T.clickListener(clickListener: (PreferenceModel) -> Boolean): T {
    clickListener(object : PreferenceModel.ClickListener {
        override fun onPreferenceClicked(preference: PreferenceModel): Boolean {
            return clickListener.invoke(preference)
        }
    })
    return this
}

fun <T : PreferenceModelBuilder_> T.changeListener(changeListener: (PreferenceModel, Any) -> Boolean): T {
    changeListener(object : PreferenceModel.ChangeListener {
        override fun onPreferenceChange(preference: PreferenceModel, newValue: Any): Boolean {
            return changeListener.invoke(preference, newValue)
        }
    })
    return this
}

fun <T : PreferenceModelBuilder_> T.intentClickListener(intent: Intent): T {
    clickListener(IntentPreferenceModelClickListener(intent))
    return this
}

fun <T : PreferenceModelBuilder_> T.intentClickListener(intent: (PreferenceModel) -> Intent?): T {
    clickListener(IntentPreferenceModelClickListener(intent))
    return this
}

fun <T : PreferenceModelBuilder_> T.urlClickListener(url: String): T {
    clickListener(UrlPreferenceModelClickListener(url))
    return this
}

fun <T : PreferenceModelBuilder_> T.urlClickListener(url: (PreferenceModel) -> String?): T {
    clickListener(UrlPreferenceModelClickListener(url))
    return this
}