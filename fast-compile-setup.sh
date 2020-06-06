#!/bin/bash
if [ "`grep "<directory>/tmp/target" pom.xml`" == "" ]; then
    sed -i s/"<build>"/"<build>\n\t\t<directory>\/tmp\/target-\${project.groupId}-\${project.artifactId}<\/directory>"/ pom.xml
    echo "fast compile enabled"
else
    echo "fast compile already enabled"
fi
