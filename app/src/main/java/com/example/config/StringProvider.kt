package com.example.config

import android.content.Context

class StringProvider( private val context: Context) {

    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}