Experimental API
================

After using this library for a while, I started to work on a new iteration of the API.
Although being only a draft, the goal and design intent of the new API has been detailed on this roadmap_.

The whole kwik library should be considered as experimental. But this new API is even more bleeding edge, far from complete and
very likely to receive breaking changes. That is why this new API is marked with the ``@ExperimentalKwikApi`` annotation.

If you want to try it out, you can opt-in with ``@OptIn(ExperimentalApi)``.
Alternatively you can or add ``-Xopt-in=com.github.jcornaz.kwik.ExperimentalKwiKApi`` to your compiler flags.

.. _roadmap: https://github.com/jcornaz/kwik/blob/main/ROADMAP.md
