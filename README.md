# testTask
It's an example of REST API application developed using Spring Boot 2.

Prerequisites: Java SDK, Maven, MySql (optional).

There are two running profiles: default that works with embedded H2 db and `mysql` to use MySql db.

If you want to try run app with mysql - edit database connection properties in application-mysql.properties file.

```
spring.datasource.url=jdbc:mysql://${mysql.host:localhost}:3306/test?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=${mysql.username:root}
spring.datasource.password=${mysql.password:}
```

Or you can set `mysql.host`, `mysql.username`, `mysql.password` in any handy way - env variable, command line arguments, etc.

Run application:
- with enabled mysql 

```mvn spring-boot:run -Dspring-boot.run.profiles=mysql```
- or with default profile on
```mvn spring-boot:run ```

## API call examples

POST localhost:8080/user

```
{ "login": "anonym",
  "fullName":"A A",
  "birthDate": "1970-01-01"
}
```

PUT localhost:8080/user/{uuidFromCreateCallResponse}

```
{ "login": "!anonym!",
  "fullName":"AA AA",
  "birthDate": "1970-01-01"
}
```

GET localhost:8080/user/{uuidFromCreateCallResponse}
