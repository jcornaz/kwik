package com.github.jcornaz.kwik.evaluator

@Suppress("MagicNumber")
actual val kwikDefaultIterations: Int
    get() = System.getProperty("kwik.iterations")?.toIntOrNull() ?: 200
