package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import java.util.*
import kotlin.random.Random

/**
 * Returns a generator of [UUID]
 */
public fun Generator.Companion.uuids(): Generator<UUID> =
    Generator { random: Random -> UUID(random.nextLong(), random.nextLong()) }
