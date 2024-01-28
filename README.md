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
- Any container runtime (Docker, Podman, Minikube, etc)

## Assumptions

- Some domain classes ar considered not variable, like Priority or Sales channel. On the contrary region, country, item type and sales channel are
  considered variable entities.
- Ids and uuids are considered uniques values for each entity.
- Every import will be removed the previous data and replaced with the new one.

## Extructure of the project

The project is developed following the hexagonal architecture. The main idea is to separate the domain logic from the infrastructure logic.
As usual the project is divided in three main packages: **domain**, **application** and **infrastructure**.
Also there are another package called **instrumentation** that contains the observability logic.

## Database

The database used is PostgreSQL. For this reason the database is deployed in a container managed by testcontainers library for testing phases.
However, the application is prepared to be deployed with other relational database but it is not tested.
The project avoid native queries and use JPA instead.

### Schema

```
        ┌────────┐ N     1 ┌─────────┐
        │ Order  ├────────►│ Country │
        └┬──┬─┬──┘         └─────────┘
         │N │N│ N
         │  │ │          1 ┌─────────┐
         │  │ └───────────►│ Region  │
         │  │              └─────────┘
         │  │
         │  │            1 ┌─────────┐
         │  └─────────────►│Item type│
         │                 └─────────┘
         │
         │               1 ┌─────────────┐
         └────────────────►│Sales channel│
                           └─────────────┘
```

### Database initialization

The database should be initialized with the data provided in the file **schema.sql**.

## Configuration








