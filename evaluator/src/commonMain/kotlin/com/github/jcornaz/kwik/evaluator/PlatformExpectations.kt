@file: Suppress("UnusedPrivateMember")

package com.github.jcornaz.kwik.evaluator

internal expect fun getEnv(name: String): String?
internal expect fun getProperty(name: String): String?
