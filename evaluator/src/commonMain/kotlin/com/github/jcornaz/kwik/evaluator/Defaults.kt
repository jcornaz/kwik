package com.github.jcornaz.kwik.evaluator

import kotlin.random.Random


/**
 * Default number of iterations for [forAll]
 */
@Suppress("MagicNumber")
val kwikDefaultIterations: Int
    get() =
        getProperty("kwik.iterations")?.toIntOrNull()
            ?: getEnv("KWIK_ITERATIONS")?.toIntOrNull()
            ?: 200

/**
 * Obtain a new seed to be used in [forAll]
 *
 * Can return a different value at each call
 *
 * May return the same value at each call
 * (if system property 'kwik.seed' or environment variable 'KWIK_ITERATIONS' is set)
 */
fun nextSeed(): Long {
    return getProperty("kwik.seed")?.toLongOrNull()
        ?: getEnv("KWIK_ITERATIONS")?.toLongOrNull()
        ?: Random.nextLong()
}
