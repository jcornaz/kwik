# Contribute to Kwik

## Participate to [issues](https://github.com/jcornaz/kwik/issues)

Feel free to create feature request and bug report there.
Only make sure that your request/report is not a duplicate of another exiting issue.

## Propose changes

### Build from source

Make sure that the environment variable `JAVA_HOME` point to a JDK installation (preferably java 11)

This is a standard gradle setup. Here the most important tasks you may want to run:

* `./gradlew check` Build and test everything. (First think to do on a fresh clone)
* `./gradlew version` Prints Kwik version
* `./gradlew sphinx` Generate documentation (can be found in `build/site/index.html`)
* `./gradlew detekt` Static code analysis and report code smells (may fail if too many code smells are found)

### Coding standard

Follow the [Kotlin conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)

Use `./gradlew detekt` to make sure to not introduce new code smells.  
 
### Open a pull request

Pull requests are very welcome.

Here are few tricks that will make your pull requests most likely to be accepted and hassle-free:

* Make sure the change is wanted by discussing it first int the [issues](https://github.com/jcornaz/kwik/issues)
   * You may skipped this test if you are convinced it will be accepted (like obvious bug fix or so), but you.
     But keep in mind what is obvious for you, is not necessarily obvious for everybody. 
* Keep your pull request small (many small PR are better than one big)
* Write automated tests covering the new feature or fix
* Make sure it passes the build (run `./gradlew check`)
* Write a description
  * explaining what problem is solved (with a reference to an existing issue if applicable)
  * helping to read and understand the code changes 
