# Описание:

RestAPI приложение для отслеживания привычек.

## Стек:

Java 17, JUnit, AssertJ, Mockito, Gradle, Jdbc,
TestContainers, PostgreSQL, Liquibase, Servlets, AspectJ,
MapStruct, Jackson, Docker
Spring Web, Spring MVC, Spring Test, Spring AOP,
OpenAPI swagger, Spring Boot, custom starters

## Инструкция по запуску проекта:

Требования

- Java 17
- Gradle

```bash
git clone https://github.com/ExToozy/java_intensive.git
cd java_intensive
git checkout homework5
cd .\aspect_starters\audit_aspect_starter\
gradle publishToMavenLocal
cd ..\logging_aspect_starter\
gradle publishToMavenLocal 
docker-compose up -d
cd ../..
gradle bootRun
```

### Эндпоинты доступны по:

- http://localhost:8080/swagger-ui.html
