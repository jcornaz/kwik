package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import java.util.*

/**
 * Returns a generator of [UUID]
 */
fun Generator.Companion.uuids(): Generator<UUID> =
    create { random ->  UUID(random.nextLong(), random.nextLong()) }
