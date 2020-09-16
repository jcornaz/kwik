# Contribute to Kwik

Feedback and pull requests are very welcome.

## Ask for help, feature or report a bug

Feel free to create feature request and bug report in the [issues](https://github.com/jcornaz/kwik/issues).
Only make sure that your request/report is not the duplicate of an exiting issue.

You may also come discuss or ask for help on
[gitter](https://gitter.im/kwik-test/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badg)


## Choose an issue to work on

Issues marked [up-for-grabs](https://github.com/jcornaz/kwik/issues?q=is%3Aissue+is%3Aopen+label%3A%22up+for+grabs%22)
are supposed to be easy and isolated enough to be done by anyone having interest in contributing.

I assign myself to issues when I am working on them. So you can safely pick any
[unassigned issue](https://github.com/jcornaz/kwik/issues?utf8=%E2%9C%93&q=is%3Aissue+is%3Aopen+no%3Aassignee+).

You may (but don't have to) write a message in the issue to say you are working on it.


## Build from source

Make sure that the environment variable `JAVA_HOME` points to a JDK installation (version 8, 11 or 15)

This project uses [gradle](https://gradle.org/). Here are the most important tasks you may want to run:

* `./gradlew check` Build and test/check everything (Best first task for a fresh clone).
* `./gradlew test` Compile sources and run unit tests
* `./gradlew sphinx` Generate documentation (result will be in `build/site/index.html`)
* `./gradlew detekt` Run a static code analysis and report code smells (may fail if too many code smells are found)


## Coding standard

Follow the [Kotlin conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)

Favor [immutable](https://en.wikipedia.org/wiki/Immutable_object) types and
[pure](https://en.wikipedia.org/wiki/Pure_function) functions

Run the static code analysis (`./gradlew detekt`) to get a reports of the code smells.


## Open a pull request

* Make sure the change is wanted by discussing it first in the [issues](https://github.com/jcornaz/kwik/issues)
* Keep your pull request small, and split it in many smaller ones if necessary
  * a pull request that solves only part of an issue, is perfectly fine.
    It might still be merged and the issue split into many smaller ones.
* Write automated tests covering the new feature or fix
  * if you are not sure how to test your changes, open the pull request as Draft.
    I'll gladly help you to write the tests.
* Make sure the build passes (run `./gradlew check`)
* Write a description
  * explain what problem is solved (with a reference to an existing issue if applicable)
  * help to read and understand the code changes
  * point parts that requires special attention or consideration
* Update documentation if necessary
  * Documentation sources are in folder `docs`
  * you can generate it locally with `./gradlew sphinx`
  * locally generated documentation will be available in `build/site/index.html`

In case you are not sure about something, it is better to open a pull request early (as a draft) and discuss it ;-)
