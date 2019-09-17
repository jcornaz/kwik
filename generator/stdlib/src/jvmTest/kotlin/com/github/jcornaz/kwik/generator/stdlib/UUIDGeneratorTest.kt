package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import java.util.*

class UUIDGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<UUID> = Generator.uuids()
}
