# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot application named "runners" built with Java 17 and Gradle. It's configured as a web application with JPA data access and Thymeleaf templating, designed to work with a MySQL database.

## Development Commands

### Build and Run
- `./gradlew build` - Build the entire project (compile, test, package)
- `./gradlew bootRun` - Run the Spring Boot application
- `./gradlew bootJar` - Create an executable JAR file
- `./gradlew clean` - Clean build artifacts

### Testing
- `./gradlew test` - Run all tests
- `./gradlew check` - Run all checks including tests

### Database Configuration
The application supports multiple database configurations using Spring profiles:

#### Development Environment (default)
- Profile: `dev` (active by default)
- Database: H2 in-memory database
- No environment variables required
- H2 Console available at: http://localhost:9281/h2-console

#### Production Environment
- Profile: `prod`
- Database: MySQL
- Required environment variables:
  - `DB_USERNAME` - MySQL database username
  - `DB_PASSWORD` - MySQL database password
- MySQL connection: `localhost:3306/test1`

#### Running with specific profiles:
- Development: `./gradlew bootRun` (default)
- Production: `DB_USERNAME=root DB_PASSWORD=password ./gradlew bootRun --args='--spring.profiles.active=prod'`

The application runs on port 9281.

## Project Structure

```
src/
├── main/
│   ├── java/com/run/runners/     # Main application code
│   └── resources/
│       └── application.yml       # Application configuration
└── test/
    └── java/com/run/runners/     # Test code
```

## Architecture

- **Framework**: Spring Boot 3.5.5 with Spring Web MVC
- **Database**: Spring Data JPA with MySQL connector
- **Template Engine**: Thymeleaf for server-side rendering
- **Build Tool**: Gradle with Java 17 toolchain
- **Development**: Spring Boot DevTools enabled for hot reloading
- **Testing**: JUnit 5 platform with Spring Boot Test

The application follows standard Spring Boot conventions with the main application class at `com.run.runners.RunnersApplication`.