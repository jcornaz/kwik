# Release process

1. Verify: `./gradlew check && ./gradlew -p example check`
2. Create a version: `npx standard-version` (eventually append `--prerelease alpha`, or beta or rc)
3. Push the created commit and tag: `git push --follow-tags`

That will trigger the publication of the artifacts from github action.
