#!/bin/bash

# Check if java is installed and the version is at least 23
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java 23 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ "${JAVA_VERSION%%.*}" -lt 23 ]]; then
    echo "Java version is $JAVA_VERSION. Please install Java 23 or higher."
    exit 1
fi

###########################
##### Pass the params #####
###########################

# Fetch from sh arguments or set defaults
if [ $# -gt 0 ]; then
  COMMAND=$1
else
  COMMAND="from-internal-to-ieee754"
fi

if [ $# -gt 1 ]; then
  INPUT=$2
else
  INPUT="8F7FFF00"
fi

# Run the CRC calculation with the specified parameters
java -jar ./build/libs/modbus-binary-formater.jar "$COMMAND" "$INPUT"
