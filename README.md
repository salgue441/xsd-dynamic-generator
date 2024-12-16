# Class Generator

## Overview

This project generates Java classes from XSD schema files using the XJC tool (XML Schema to Java Compiler). It enhances the generated classes by enabling the Singleton design pattern when required, using an object factory to control instance creation.

## Features

- **XSD to Java Transformation**: converts XML schema definitions into Java classes.
- **Singleton Support**: generated classes can follow the Singleton design pattern for optimized memory management.
- **Object Factory**: factory class that controls the creation of instances of the generated classes.

## Prerequisites

### Dependencies

Key dependencies are managed using Maven and specified in `pom.xml`. Highlights include:

- **jaxb-xjc**: Java Architecture for XML Binding (JAXB) module for XJC.
- **JUnit**: testing framework for unit tests.
- **Maven Exec Plugin**: plugin for executing Java programs.

## Project Structure

```bash
CLASS_GENERATOR/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── exception/       # Custom exception handling
│   │   │       ├── generated/       # XJC-generated Java classes
│   │   │       ├── strategy/        # Strategies for customization
│   │   │       ├── transformer/     # Class for XSD transformation
│   │   │       │   └── XsdToSingletonGenerator.java
│   │   └── resources/               # XSD files and configurations
│   ├── test/                        # Unit tests
├── target/                          # Compiled output
├── pom.xml                          # Maven build configuration
└── README.md                        # Project documentation
```

## How to Run

### Linux

1. Generate java classes from XSD

Run the following command to generate the classes.

```bash
./run.sh
```

If the script fails to run, give it execute permissions.

```bash
chmod +x run.sh
```

### Windows

1. Generate java classes from XSD

Run the following command to generate the classes.

```bash
run.bat
```
