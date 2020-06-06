#!/bin/bash

# see in pom.xml: <build/><directory/>
# https://maven.apache.org/guides/introduction/introduction-to-the-pom.html
export TARGET_WEBAPP=/tmp/target-image.exifweb-webapp

# export JAVA_HOME=/usr/lib/jvm/java-8-oracle
# export JAVA_HOME="/home/adr/.sdkman/candidates/java/11.0.2-open"

if [ -e ./mvnw ]; then
	echo "using ./mvnw"
	MVN="./mvnw"
elif [ -e ../mvnw ]; then
	echo "using ../mvnw"
	MVN="../mvnw"
elif [ -e mvnw ]; then
	echo "using mvnw"
	MVN="mvnw"
elif [ "`which mvn`" != "" ]; then
	echo "using `which mvn`"
	MVN="`which mvn`"
else
    echo "Can't find any of \"mvn\" or \"mvnw\"!"
    exit 1
fi
export MVN="$MVN -e"

# export JAVA_HOME=$TOOLS/jdk1.8.0_141
# export M2_HOME=$TOOLS/maven-3.3.9
# export M2_CONF=$M2_HOME/conf/settings-jisr.xml
# export MVN="$M2_HOME/bin/mvn -s \"$M2_CONF\""
