#!/usr/bin/env bash
server=$1
./gradlew assemble
jar=$(find build/libs/ -type f -name '*.jar' | awk -F './' '{print $3}')
echo "find jar : $jar"
echo "$server"
scp build/libs/${jar} ${server}