package com.github.jcornaz.kwik.evaluator

import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun getEnv(name: String): String? =
    getenv(name)?.toKString()?.takeUnless { it.isBlank() }

internal actual fun getProperty(name: String): String? =
    null
