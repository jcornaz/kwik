package com.github.jcornaz.kwik.evaluator


internal actual fun getEnv(name: String): String? =
    System.getenv(name)

internal actual fun getProperty(name: String): String? =
    System.getProperty(name)
