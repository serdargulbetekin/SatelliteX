package com.example.config

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: Int, toastLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(message), toastLength).show()
}
fun Context.showToast(message: String, toastLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, toastLength).show()
}