#!/usr/bin/env bash
profile=$1
echo "$profile"
if [ ! $profile ]; then
  echo "profile is null"
else
  ./gradlew clean -Pprofile=$profile
  ./gradlew assemble -Pprofile=$profile
  jar=$(find build/libs/ -type f -name '*.jar' | awk -F './' '{print $3}')
  echo "find jar : $jar"

  java -jar build/libs/$jar --spring.profiles.active=$profile
fi
