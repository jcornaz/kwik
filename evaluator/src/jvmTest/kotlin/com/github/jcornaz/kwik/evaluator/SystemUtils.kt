package com.github.jcornaz.kwik.evaluator

fun setOrClearProperty(key: String, value: String?) {
    if (value == null) {
        System.clearProperty(key)
    } else {
        System.setProperty(key, value)
    }
}

inline fun <R> withSystemProperty(key: String, value: String?, block: () -> R): R {
    val previousValue: String? = System.getProperty(key)
    setOrClearProperty(key, value)
    return try {
        block()
    } finally {
        setOrClearProperty(key, previousValue)
    }
}
