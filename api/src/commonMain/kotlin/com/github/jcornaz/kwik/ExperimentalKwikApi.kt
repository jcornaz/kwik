package com.github.jcornaz.kwik

/**
 * This annotation denotes Kwik's experimental API. It might not be complete,
 * and there is a good change that breaking changes will be made in the future.
 */
@RequiresOptIn(
    """
    This is highly experimental API. It might not be complete,
    and there is a good change that breaking changes will be made in the future.
    """,
    level = RequiresOptIn.Level.WARNING
)
@Retention(AnnotationRetention.BINARY)
public annotation class ExperimentalKwikApi
