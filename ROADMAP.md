# The road map for version 1.0.0

This document details the direction `kwik` is taking and the `API` it would provide when reaching version `1.0.0`.

## How it will look like

### Basic test

```kotlin
@Test
fun isAssociative() = forAny(Arb.pair(Arb.int(), Arb.int())) { (x, y) ->
    (x + y) alwaysEquals (y + x)
}
```

Few points to note:

* Instead of providing multiple overloads of `forAny` with different number of arguments, it would take exactly one fuzzer argument, I'd let the user create explicit combination.
* The lambda `forAny` would need to return an instance of `TestResult` (which in this example, is created by the call of `alwaysEqual`)

This has for consequences, that correct usage can be fully checked at compile time. The compiler will make sure for us that:

* Data can be generated (so the test can pass)
* An expectation/assertion has been formulated (so the test can fail)

All of that without loosing flexibility, fluency or information in case of failure.

### Skipping an evaluation

To skip an evaluation the user could either `filter` the arbitrary or return `TestResult.Skip` in evaluation:

```kotlin
forAny(Arb.pair(Arb.int(), Arb.int())) { (x, y) ->
    if (x == y) {
        TestResult.Skip
    } else {
        (x - y) neverEquals 0
    }
}
```

### Test result

As demonstrated in the 2 previous examples, a `TestResult` api would be provided to let the user formulate expectation and control the evaluation.

This is heavily inspired from `TestResult` in rust's [quickcheck](https://github.com/BurntSushi/quickcheck) and `Expectation` in elm's [elm-test](https://github.com/elm-explorations/test).

An api for fluent formulation of expectation would be provided. Mostly inspired from [Kluent](https://github.com/MarkusAmshove/Kluent), with slight different naming using `always` and `never` instead of `should` and `shouldNot`.

The different naming has two advantages:

* Don't cause confusion in codebases where `kwik` would cohabit with a fluent assertion library such as Kluent, Kotest or similar.
* Emphasis the *invariant* flavor of the test. Since it is not an example, but a rule that should always (or never) be satisfied.

### Configuration

I'd give priority on configuration made via system property and environment variable.
It proved to be very powerful and can remove the need to edit the source code (with hard-coded config) when playing to reproducing errors.

It would still be possible to configure a *min* and *max* number of iterations from code.  

```kotlin
forAny(Arb.int(), iterations = 10..100) { x ->
    -(-x) alwaysEqual x
} 
```

The actual number of iterations depends then on the global configuration. The code only defines the sensible limits in regard of the system under test.

The random generation would still be seeded. But the seed will only be configurable via system property or environment variable. Not from code anymore.

### Composing and configuring fuzzers

`map`, `filter`, `andThen` (aka flatmap), `combine` and `merge` would be the main building block that should allow easy and safe creation of a wide variety of fuzzers.

`withSamples` and common aliases such as `withNull`, `withNaN`, etc. proved to be very useful and would be ported in the new api.

## Major tasks

* [x] Introduce TestResult with a minimal api (#85)
* [ ] Provide a fuzzer API that can be used the same way as generator are currently used (#237)
* [ ] Document usage of the new API (#243)
* [ ] Provide decent support for input simplification (aka shrinking) (#62, #173, #174, #64)
* [ ] Move the concept of 'sample' from generators to fuzzers (#186)
* [ ] Deprecate old API and document how to migrate the new API (the more can be automated via Kotlin's deprecation system, the better)
