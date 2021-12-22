package com.example.config

import com.example.satellitex.R
import io.reactivex.disposables.Disposable

fun Throwable?.optMessage(stringProvider: StringProvider): String {
    return this?.message.takeIf { !it.isNullOrEmpty() }
        ?: stringProvider.getString(R.string.general_error_message)
}

fun Disposable.disposeIfNotDisposed() {
    if (!isDisposed) {
        dispose()
    }
}