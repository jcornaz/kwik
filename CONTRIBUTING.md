# Contribute to Kwik

## Ask for feature or report a bug

Feel free to create feature request and bug report in the [issues](https://github.com/jcornaz/kwik/issues).
Only make sure that your request/report is not a duplicate of an exiting one.

## Propose changes

### Build from source

Make sure that the environment variable `JAVA_HOME` point to a JDK installation (preferably java 11)

This is a standard gradle setup. Here are the most important tasks you may want to run:

* `./gradlew check` Build and test/check everything (Best first task for a fresh clone)
* `./gradlew test` Compile sources and run unit tests
* `./gradlew sphinx` Generate documentation (result will be in `build/site/index.html`)
* `./gradlew detekt` Static code analysis and report code smells (may fail if too many code smells are found)

### Coding standard

Follow the [Kotlin conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)

Use `./gradlew detekt` to make sure to not introduce new code smells.  
 
### Open a pull request

Pull requests are very welcome.

Here are few tricks that will make your pull requests most likely to be accepted and hassle-free:

* Make sure the change is wanted by discussing it first int the [issues](https://github.com/jcornaz/kwik/issues)
* Keep your pull request small (many small PR are better than one big)
  * **Don't be afraid to open a pull request that solves only part of an issue**. It might still be merged and the issue be splitted into smaller ones.
* Write automated tests covering the new feature or fix
* Make sure it passes the build (run `./gradlew check`)
* Write a description
  * explaining what problem is solved (with a reference to an existing issue if applicable)
  * helping to read and understand the code changes
* Update documentation if necessary (located in folder `docs`, you can generate it locally with `./gradlew sphinx`)
