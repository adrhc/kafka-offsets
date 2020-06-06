#!/bin/bash

if [ -e "env.sh" ]; then
	source env.sh
elif [ -e "../env.sh" ]; then
    source ../env.sh
fi

# disable all tests:
# ./mvnw -e -Dmaven.javadoc.skip=true -Dmaven.test.skip=true clean install

# these require <forkCount> usage:
# ./clean.install.sh -P db-off	        -> equivalent to in-memory-only (default)
# ./clean.install.sh -P db-on
#
# these does not require <forkCount> usage:
# ./clean.install.sh -P staging-only
# ./clean.install.sh -P production-only
$MVN -e -Dmaven.javadoc.skip=true clean package "$@"
