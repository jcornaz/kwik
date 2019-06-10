#!/bin/bash


if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
    ./gradlew check bintrayUpload --no-daemon --no-build-cache
else
    ./gradlew check
fi
