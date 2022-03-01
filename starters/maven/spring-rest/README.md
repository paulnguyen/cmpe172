# Spring Rest API

* https://spring.io/guides/tutorials/rest/

## Version 1.0

* Employee CRUD Operations via REST API
* Using H2 SQL Database (In-Memory)
	* URL:  http://localhost:8080/h2-console/
	* Driver: org.h2.Driver
	* JDBC URL:  jdbc:h2:mem:devDb
	* DB User: sa
	* DB Password: password 


## Version 2.0

* Modification of v1.0 to use Spring HATEOAS


## Version 3.0

* Modification of v2.0 to add Orders


## JPA Config Examples

### H2 Database

```
# H2 DB Console: http://localhost:8080/h2-console
# REF:  https://www.baeldung.com/spring-boot-h2-database 

spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:cmpe172
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### MySQL Database

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/cmpe172
spring.datasource.username=admin
spring.datasource.password=cmpe172
```

