package com.github.jcornaz.kwik.fuzzer

import com.github.jcornaz.kwik.generator.api.Generator

data class Fuzzer<T>(
    val generator: Generator<T>,
    val guarantees: List<(T) -> Boolean>
)
