package com.github.jcornaz.kwik.evaluator


/**
 * Default number of iterations for [forAll]
 */
@Suppress("MagicNumber")
val kwikDefaultIterations: Int
    get() =
        getProperty("kwik.iterations")?.toIntOrNull()
            ?: getEnv("KWIK_ITERATIONS")?.toIntOrNull()
            ?: 200
