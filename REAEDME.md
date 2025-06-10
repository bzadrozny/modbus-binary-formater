# Project Title: SCZ Converter Tool

## Overview

The SCZ Converter Tool is a command-line utility designed to convert data between different formats, including IEEE 754 floating-point representation and
internal binary formats. It supports conversions in both directions and provides additional utilities for string-to-binary conversions.

## Features

- **Ieee754ToInternalCmd**: Converts IEEE 754 floating-point numbers to internal binary format.
- **InternalToIeee754Cmd**: Converts internal binary format back to IEEE 754 floating-point numbers.
- **ConverterCmd**: A general command for converting various data formats.
- **ToolCmd**: Provides additional tools and utilities for data manipulation.
- **BiDirectConverter**: A bi-directional converter that can handle multiple encoding formats.
- **StringToBinaryConverter**: Converts strings to binary format based on specified encodings.

## Build

To build the project, you can use Gradle. The project is structured to allow easy compilation and execution of the commands.

```bash
./gradlew clean build
```

Build artifacts will be generated in the `build/libs` directory.

## Usage

Run the tool from the command line with the appropriate commands.

Available commands include:

- `from-ieee754-to-internal`: Converts a value from IEEE 754 format to internal binary format.
- `from-internal-to-ieee754`: Converts a value from internal binary format back to IEEE 754 format.

Input values should be provided in hexadecimal or binary natural format.

You can run the tool using the following command:

```bash
java -jar modbus-binary-formater.jar from-ieee754-to-internal "8F7FFF00U"
java -jar modbus-binary-formater.jar from-internal-to-ieee754 "10001111011111111111111100011001"
```

## Requirements

- Kotlin runtime environment
- Java Development Kit (JDK)

## Installation

Clone the repository and build the project using Gradle or your preferred Kotlin build tool.
