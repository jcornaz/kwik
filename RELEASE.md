# Release process

### Prerequisites

* An environment variable `JAVA_HOME` must point to a valid JDK 11 or newer

### Release steps

1. Verify: `./gradlew check && ./gradlew -p example check -Dinclude=true`
2. Update `CHANGELOG.md` and `gradle.properties`
3. Commit the changes `git add -A && git commit`
4. Create version tag
5. Push the created commit and tag: `git push --follow-tags`
6. Manually create a github release at the newly created tag.
   Copy/Paste the changelog entry generated by `standard-version` for the github release description

That will trigger the publication of the artifacts from github action.
