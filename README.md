# Outback Weather Tracker

## Overview

**Outback Weather Tracker** is a Java-based application designed to track and analyze detailed weather events for the outback. This project provides a comprehensive solution for monitoring weather conditions and managing data relevant to the unique environment of the outback.

## Features

- **Detailed Weather Tracking**: Monitor and record various weather parameters including temperature, humidity, wind speed, and precipitation.
- **Historical Data Analysis**: Analyze historical weather data to identify trends and patterns.
- **Real-Time Updates**: Receive real-time updates and alerts for significant weather events.
- **User-Friendly Interface**: Easy-to-use graphical interface for interacting with the weather data.
- **Data Export**: Export weather data for use in reports and further analysis.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) amazon corretto 21
- Maven 3.8 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/CAB302-Software-Development/CAB302_OutbackWeatherTracker.git

### Building

1. run build mvn command:
   ```bash
   mvn clean install

### Testing

1. run test mvn command:
   ```bash
   mvn test

# Project Dependencies

This document outlines the Maven dependencies used in the **Outback Weather Tracker** project. Each dependency is crucial for the project's functionality and is managed through Maven.

## Dependencies List

### 1. SQLite JDBC
- **GroupId**: `org.xerial`
- **ArtifactId**: `sqlite-jdbc`
- **Version**: `3.46.0.1`
- **Maven Repository**: [SQLite JDBC](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)

### 2. JavaFX Controls
- **GroupId**: `org.openjfx`
- **ArtifactId**: `javafx-controls`
- **Version**: `21`
- **Maven Repository**: [JavaFX Controls](https://mvnrepository.com/artifact/org.openjfx/javafx-controls/21)

### 3. JavaFX FXML
- **GroupId**: `org.openjfx`
- **ArtifactId**: `javafx-fxml`
- **Version**: `21`
- **Maven Repository**: [JavaFX FXML](https://mvnrepository.com/artifact/org.openjfx/javafx-fxml/21)

### 4. ControlsFX
- **GroupId**: `org.controlsfx`
- **ArtifactId**: `controlsfx`
- **Version**: `11.1.2`
- **Maven Repository**: [ControlsFX](https://mvnrepository.com/artifact/org.controlsfx/controlsfx/11.1.2)

### 5. BootstrapFX Core
- **GroupId**: `org.kordamp.bootstrapfx`
- **ArtifactId**: `bootstrapfx-core`
- **Version**: `0.4.0`
- **Maven Repository**: [BootstrapFX Core](https://mvnrepository.com/artifact/org.kordamp.bootstrapfx/bootstrapfx-core/0.4.0)

### 6. JUnit Jupiter API
- **GroupId**: `org.junit.jupiter`
- **ArtifactId**: `junit-jupiter-api`
- **Version**: `${junit.version}`
- **Scope**: `test`
- **Maven Repository**: [JUnit Jupiter API](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)

### 7. JUnit Jupiter Engine
- **GroupId**: `org.junit.jupiter`
- **ArtifactId**: `junit-jupiter-engine`
- **Version**: `${junit.version}`
- **Scope**: `test`
- **Maven Repository**: [JUnit Jupiter Engine](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine)

### 8. TestFX Core
- **GroupId**: `org.testfx`
- **ArtifactId**: `testfx-core`
- **Version**: `4.0.16-alpha`
- **Scope**: `test`
- **Maven Repository**: [TestFX Core](https://mvnrepository.com/artifact/org.testfx/testfx-core/4.0.16-alpha)

### 9. TestFX JUnit5
- **GroupId**: `org.testfx`
- **ArtifactId**: `testfx-junit5`
- **Version**: `4.0.16-alpha`
- **Scope**: `test`
- **Maven Repository**: [TestFX JUnit5](https://mvnrepository.com/artifact/org.testfx/testfx-junit5/4.0.16-alpha)
