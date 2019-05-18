# Change log
List of notable changes to this project.

The format is based on https://keepachangelog.com/en/1.0.0/[Keep a Changelog], +
and this project adheres to https://semver.org/spec/v2.0.0.html[Semantic Versioning].

## Current version
### Added
* `ints()`, `longs()`, `floats()`, `doubles()` and `boolean()` generators
* `map` operator to transform an existing generator
* `Generator.of()` to create a generate from a finite set of samples
* `enum()` to create a generator from an enum

## [0.1.0-alpha.1](https://github.com/jcornaz/kwik/tree/0.1.0-alpha.1)
### Added
* `Generator` interface for generating random values
* `randomSequence` helper to easily create a random (yet predictable) sequence of value
* `forAll` and `checkForAll` function to assess a property of the system under test.
* `withSample` and `withNull` to inject constants values to be always tested
* `zip` operator to combine two given generators
