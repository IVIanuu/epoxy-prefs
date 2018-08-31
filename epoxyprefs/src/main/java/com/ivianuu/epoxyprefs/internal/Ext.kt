package com.ivianuu.epoxyprefs.internal

import android.support.v4.text.PrecomputedTextCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.AppCompatTextView
import android.widget.TextView
import java.util.concurrent.Executor

internal fun TextView.setTextFuture(
    text: CharSequence,
    params: PrecomputedTextCompat.Params = TextViewCompat.getTextMetricsParams(this),
    executor: Executor? = null
) {
    (this as? AppCompatTextView)
        ?.setTextFuture(PrecomputedTextCompat.getTextFuture(text, params, executor))
}