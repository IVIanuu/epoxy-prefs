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

package com.ivianuu.epoxyprefs.util

import android.content.Intent
import android.net.Uri
import com.ivianuu.epoxyprefs.PreferenceModel

/**
 * Launches urls on pref clicks
 */
class UrlPreferenceModelClickListener : IntentPreferenceModelClickListener {

    constructor(url: String) : super(buildIntentForUrl(url)!!)

    constructor(url: (PreferenceModel) -> String?) : super({
        buildIntentForUrl(url.invoke(it))
    })

    private companion object {
        private fun buildIntentForUrl(url: String?): Intent? {
            if (url == null) {
                return null
            }

            return Intent(Intent.ACTION_VIEW).apply {
                var realUrl = url
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    realUrl = "http://$url"
                }
                data = Uri.parse(realUrl)
            }
        }
    }
}