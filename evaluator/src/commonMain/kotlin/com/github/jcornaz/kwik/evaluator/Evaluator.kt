package com.github.jcornaz.kwik.evaluator

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.combineWith
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.stdlib.default
import kotlin.random.Random

/**
 * Default number of iterations for [forAll]
 */
expect val kwikDefaultIterations: Int

/**
 * Call multiple times [property] with random values generated by the given [generator]
 *
 * The [property] must return a boolean representing if the property is satisfied
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must return a boolean (true = satisfied, false = falsified)
 */
fun <T> forAll(
    generator: Generator<T>,
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    property: PropertyEvaluationContext.(T) -> Boolean
) {
    require(iterations > 0) { "Iterations must be > 0, but was: $iterations" }

    val context = PropertyEvaluationContextImpl

    var attempts = 0
    val iterator = generator.testValues(seed).iterator()

    while (attempts < iterations) {
        val argument = iterator.next()

        @Suppress("SwallowedException") // SkipEvaluation is used to silently skip an evaluation
        val isSatisfied = try {
            context.property(argument).also { ++attempts }
        } catch (skip: SkipEvaluation) {
            true
        } catch (error: Throwable) {
            throw FalsifiedPropertyError(attempts + 1, iterations, seed, extractArgumentList(argument), error)
        }

        if (!isSatisfied) throw FalsifiedPropertyError(attempts, iterations, seed, extractArgumentList(argument))
    }

    println("OK, passed $attempts tests. (seed: $seed)")
}

private object PropertyEvaluationContextImpl : PropertyEvaluationContext {
    override fun skipIf(condition: Boolean) {
        if (condition) throw SkipEvaluation()
    }
}

private class SkipEvaluation : Throwable()

private fun extractArgumentList(argument: Any?): List<Any?> {
    return if (argument is ArgumentPair<*, *>) {
        extractArgumentList(argument.first) + extractArgumentList(
            argument.second
        )
    } else {
        listOf(argument)
    }
}

/**
 * Call multiple times [property] with random values generated by the default generator for the type [T]
 *
 * The [property] must return a boolean representing if the property is satisfied
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must return a boolean (true = satisfied, false = falsified)
 */
inline fun <reified T> forAll(
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(T) -> Boolean
): Unit = forAll<T>(Generator.default(), iterations, seed) { property(it) }

/**
 * Call multiple times [property] with random values generated by the given [generator]
 *
 * The [property] must perform assertions and throw an exception if falsified
 *
 * No exception thrown means the property is satisfied.
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must perform assertions and throw an exception if falsified
 */
inline fun <reified T> checkForAll(
    generator: Generator<T> = Generator.default(),
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(T) -> Unit
): Unit = forAll(generator, iterations, seed) {
    property(it)
    true
}

/**
 * Call multiple times [property] with random values generated by the given generators
 *
 * The [property] must return a boolean representing if the property is satisfied
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must return a boolean (true = satisfied, false = falsified)
 */
inline fun <reified A, reified B> forAll(
    generatorA: Generator<A> = Generator.default(),
    generatorB: Generator<B> = Generator.default(),
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(A, B) -> Boolean
) = forAll(generatorA.combineWith(generatorB, ::ArgumentPair), iterations, seed) { (a, b) ->
    property(a, b)
}

/**
 * Call multiple times [property] with random values generated by the given generators
 *
 * The [property] must perform assertions and throw an exception if falsified
 *
 * No exception thrown means the property is satisfied.
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must perform assertions and throw an exception if falsified
 */
inline fun <reified A, reified B> checkForAll(
    generatorA: Generator<A> = Generator.default(),
    generatorB: Generator<B> = Generator.default(),
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(A, B) -> Unit
): Unit = forAll(generatorA, generatorB, iterations, seed) { a, b ->
    property(a, b)
    true
}

/**
 * Call multiple times [property] with random values generated by the given generators
 *
 * The [property] must return a boolean representing if the property is satisfied
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must return a boolean (true = satisfied, false = falsified)
 */
inline fun <reified A, reified B, reified C> forAll(
    generatorA: Generator<A> = Generator.default(),
    generatorB: Generator<B> = Generator.default(),
    generatorC: Generator<C> = Generator.default(),
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(A, B, C) -> Boolean
): Unit = forAll(generatorA.combineWith(generatorB, ::ArgumentPair), generatorC, iterations, seed) { (a, b), c ->
    property(a, b, c)
}

/**
 * Call multiple times [property] with random values generated by the given generators
 *
 * The [property] must perform assertions and throw an exception if falsified
 *
 * No exception thrown means the property is satisfied.
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must perform assertions and throw an exception if falsified
 */
inline fun <reified A, reified B, reified C> checkForAll(
    generatorA: Generator<A> = Generator.default(),
    generatorB: Generator<B> = Generator.default(),
    generatorC: Generator<C> = Generator.default(),
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(A, B, C) -> Unit
): Unit = forAll(generatorA, generatorB, generatorC, iterations, seed) { a, b, c ->
    property(a, b, c)
    true
}

/**
 * Call multiple times [property] with random values generated by the given generators
 *
 * The [property] must return a boolean representing if the property is satisfied
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must return a boolean (true = satisfied, false = falsified)
 */
inline fun <reified A, reified B, reified C, reified D> forAll(
    generatorA: Generator<A> = Generator.default(),
    generatorB: Generator<B> = Generator.default(),
    generatorC: Generator<C> = Generator.default(),
    generatorD: Generator<D> = Generator.default(),
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(A, B, C, D) -> Boolean
): Unit = forAll(
    generatorA = generatorA.combineWith(generatorB, ::ArgumentPair),
    generatorB = generatorC.combineWith(generatorD, ::ArgumentPair),
    iterations = iterations,
    seed = seed
) { (a, b), (c, d) -> property(a, b, c, d) }

/**
 * Call multiple times [property] with random values generated by the given generators
 *
 * The [property] must perform assertions and throw an exception if falsified
 *
 * No exception thrown means the property is satisfied.
 *
 * @param iterations Number of times [property] should be called
 * @param seed Random generation seed. Random by default. Specify a value to reproduce consistent results
 * @param property Function invoked multiple times with random inputs to assess a property of the System under test.
 *                 Must perform assertions and throw an exception if falsified
 */
inline fun <reified A, reified B, reified C, reified D> checkForAll(
    generatorA: Generator<A> = Generator.default(),
    generatorB: Generator<B> = Generator.default(),
    generatorC: Generator<C> = Generator.default(),
    generatorD: Generator<D> = Generator.default(),
    iterations: Int = kwikDefaultIterations,
    seed: Long = Random.nextLong(),
    crossinline property: PropertyEvaluationContext.(A, B, C, D) -> Unit
): Unit = forAll(generatorA, generatorB, generatorC, generatorD, iterations, seed) { a, b, c, d ->
    property(a, b, c, d)
    true
}

/**
 * Pair of argument. Used by [forAll] of higher arity to let the [forAll] of argument being able to display the argument list.
 */
data class ArgumentPair<A, B>(val first: A, val second: B)

/**
 * Exception thrown when a property is falsified
 */
data class FalsifiedPropertyError(
    val attempts: Int,
    val iterations: Int,
    val seed: Long,
    val arguments: List<Any?>,
    override val cause: Throwable? = null
) : AssertionError(buildString {
    append("Property falsified after $attempts tests (out of $iterations)\n")

    arguments.forEachIndexed { index, arg ->
        append("Argument ${index + 1}: $arg\n")
    }

    append("Generation seed: $seed")
})

/**
 * Return the values to test for the given [seed].
 *
 * Start by the [Generator.samples] before emitting the [Generator.randomSequence]
 */
internal fun <T> Generator<T>.testValues(seed: Long): Sequence<T> =
    samples.asSequence() + randomSequence(seed)
