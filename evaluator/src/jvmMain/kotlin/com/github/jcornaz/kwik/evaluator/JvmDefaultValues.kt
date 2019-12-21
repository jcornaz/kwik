package com.github.jcornaz.kwik.evaluator

/**
 * Default number of iterations for [forAll]
 */
@Suppress("MagicNumber")
actual val kwikDefaultIterations: Int
    get() = System.getProperty("kwik.iterations")?.toIntOrNull() ?: 200
