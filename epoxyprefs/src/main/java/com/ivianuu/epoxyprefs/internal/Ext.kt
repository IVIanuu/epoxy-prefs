package com.ivianuu.epoxyprefs.internal

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import java.util.concurrent.Executor

internal fun TextView.setTextFuture(
    text: CharSequence,
    params: PrecomputedTextCompat.Params = TextViewCompat.getTextMetricsParams(this),
    executor: Executor? = null
) {
    (this as? AppCompatTextView)
        ?.setTextFuture(PrecomputedTextCompat.getTextFuture(text, params, executor))
}