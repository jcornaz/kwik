package com.github.jcornaz.kwik.evaluator

actual val kwikDefaultIterations: Int
    get() = System.getProperty("kwik.iterations")?.toIntOrNull() ?: 200
