package com.example.config

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.widget.TextView

fun TextView.setClickableText(content: String, start: Int = 0) {

    val commaIndex = content.indexOf(":")
    val spannableString = SpannableString(content)
    spannableString.setSpan(
        StyleSpan(Typeface.BOLD),
        start,
        if (commaIndex == -1) 0 else commaIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    text = spannableString
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}