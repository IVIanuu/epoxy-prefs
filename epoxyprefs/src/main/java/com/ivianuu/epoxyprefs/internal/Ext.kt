package com.ivianuu.epoxyprefs.internal

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat

internal fun TextView.setTextFuture(text: CharSequence) {
    if (this is AppCompatTextView) {
        setTextFuture(
            PrecomputedTextCompat.getTextFuture(
                text,
                TextViewCompat.getTextMetricsParams(this), null
            )
        )
    } else {
        setText(text)
    }
}