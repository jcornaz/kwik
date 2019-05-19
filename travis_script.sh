#!/bin/bash


if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
    gradlew check bintrayUpload sonarqube --no-daemon --no-build-cache
elif [ "$TRAVIS_PULL_REQUEST" = "false" ] || [ -z "$SONAR_TOKEN" ]
then
    gradlew check
else
    gradlew check sonarqube
fi
