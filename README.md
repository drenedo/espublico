# Coding test for "espublico tecnología"

## Requirements

To run the application

- Java >= 17

To build the application

- Java >= 17
- Maven >= 3.0

To launch tests

- Java >= 17
- Maven >= 3.0
- Any container runtime (Docker, Podman, Minikube...)

## Assumptions

- Some domain classes ar considered not variable, like Priority or Sales channel. On the contrary region, country, item type and sales channel are
  considered variable entities.
- Ids and uuids are considered uniques values for each entity. (This is not true in the given example)
- Every import will be removed the previous data and replaced with the new one.

## Extructure of the project

The project is developed following the hexagonal architecture. The main idea is to separate the domain logic from the infrastructure logic.
As usual the project is divided in three main packages: **domain**, **application** and **infrastructure**.
Also there are another package called **instrumentation** that contains the observability logic.

## Database

The database used is PostgreSQL. For this reason the database is deployed in a container managed by testcontainers library for testing phases.
However, the application is prepared to be deployed with other relational database but **it is not tested**.
The project avoid native queries and use JPA instead for this purpose.

### Schema

```
        ┌────────┐ N     1 ┌─────────┐
        │ Order  ├────────►│ Country │
        └───┬─┬──┘         └─────────┘
            │N│ N
            │ │          1 ┌─────────┐
            │ └───────────►│ Region  │
            │              └─────────┘
            │
            │            1 ┌─────────┐
            └─────────────►│Item type│
                           └─────────┘
```

### Database initialization

The database should be initialized with the data provided in the file **schema.sql**.

## Configuration variables

### Database configuration

This is a typical configuration used for the database.
The only custom value is the **orders.jpa.page-size** that is used to define the size
of transaction when persisting orders.

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.batch_size=100
spring.datasource.url=jdbc:postgresql://localhost:5432/database
spring.datasource.username=test
spring.datasource.password=test
orders.jpa.page-size=250
```

### Rest API configurations

Host of the rest API and page size of the requests.

```properties
orders.rest.url=https://kata-espublicotech.g3stiona.com
orders.http.page-size=500
```

### Development








