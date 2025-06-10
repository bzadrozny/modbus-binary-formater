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
  INPUT=$1
else
  INPUT="8F7FFF00"
fi

if [ $# -gt 1 ]; then
  INPUT_ENCODING=$2
else
  INPUT_ENCODING="INTERNAL"
fi

if [ $# -gt 2 ]; then
  OUTPUT_ENCODING=$3
else
  OUTPUT_ENCODING="IEEE754"
fi

# Run the CRC calculation with the specified parameters
java -jar ./build/libs/modbus-binary-formater.jar -i $INPUT_ENCODING -o $OUTPUT_ENCODING $INPUT
