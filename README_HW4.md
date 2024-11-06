# Описание:

RestAPI приложение для отслеживания привычек.

## Стек:

Java 17, JUnit, AssertJ, Mockito, Gradle, Jdbc,
TestContainers, PostgreSQL, Servlets, AspectJ,
MapStruct, Jackson,
Spring Web, Spring MVC, Spring Test, Spring AOP, OpenAPI swagger

## Инструкция по запуску проекта:

Требования

- Tomcat 9
- Java 17
- Gradle

```bash
git clone https://github.com/ExToozy/java_intensive.git
cd java_intensive
git checkout homework4
gradle war
gradle dockerComposeUp
cp build\habit_tracker-1.0-SNAPSHOT.war path\to\tomcat9\webapps\
path\to\tomcat\bin\startup.bat
```

### Эндпоинты доступны по:

- http://localhost:8080/swagger-ui/