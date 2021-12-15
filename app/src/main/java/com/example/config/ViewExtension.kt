package com.example.config

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.getColorInt(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(context, colorRes)
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}