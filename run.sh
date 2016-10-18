#!/usr/bin/env bash
./gradlew clean
./gradlew assemble
jar=$(find build/libs/ -type f -name '*.jar' | awk -F './' '{print $3}')
echo "find jar : $jar"

java -jar build/libs/$jar
