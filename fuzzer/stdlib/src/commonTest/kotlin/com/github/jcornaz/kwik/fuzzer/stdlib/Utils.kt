package com.github.jcornaz.kwik.fuzzer.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence


fun <T> Generator<T>.drawSample() = randomSequence(0).take(100).toList()
