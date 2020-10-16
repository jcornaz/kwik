package com.github.jcornaz.kwik.fuzzer.api

/**
 * Annotation to denote experimental fuzzer API
 */
@RequiresOptIn(
    message = """
        The fuzzer system and related abstractions are still extremely experimental and may be reworked
        without any announcement or migration aid.     
    """,
    level = RequiresOptIn.Level.WARNING
)
@Retention(AnnotationRetention.BINARY)
@Deprecated("Has been replaced by the 'ExperimentalKwikApi' annotation")
annotation class ExperimentalKwikFuzzer
