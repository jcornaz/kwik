package com.github.jcornaz.kwik

@RequiresOptIn(
    """
    This is Kwik internal API. It should not be used directly.
    It can be changed or removed at any time without prior notice.
    """,
    level = RequiresOptIn.Level.ERROR
)
annotation class InternalKwikApi

@RequiresOptIn(
    """
    This is highly experimental API. It might not be complete,
    and there is a good change that breaking changes will be made in the future.
    """,
    level = RequiresOptIn.Level.ERROR
)
annotation class ExperimentalKwikApi
