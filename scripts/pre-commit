#!/bin/sh

targetFiles=$(git diff --staged --name-only)

echo "Apply Spotless.."
./gradlew spotlessApply

for file in $targetFiles; do
  if test -f "$file"; then
    git add $file
  fi
done