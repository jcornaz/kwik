# Release process

### Prerequistes

* An environment variable `JAVA_HOME` must point to a valid JDK 11 or newer
* npm version 12 or newer must be installed

### Release steps

1. Verify: `./gradlew check && ./gradlew -p example check`
2. Create a version: `npx standard-version` (eventually append `--prerelease alpha`, or beta or rc)
3. Push the created commit and tag: `git push --follow-tags`

That will trigger the publication of the artifacts from github action.
