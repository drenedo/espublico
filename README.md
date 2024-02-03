# Coding challenge for "espublico tecnología"

## Requirements

To run the application

- Java >= 17

To build the application

- Java >= 17
- Maven >= 3.0

To verify the state of the application (tests)

- Java >= 17
- Maven >= 3.0
- Any container runtime (Docker, Podman, Minikube...)

## Important assumptions

- Some domain classes ar considered not variable, like Priority or Sales channel. On the contrary region, country and item type are
  considered variable entities.
- Ids and uuids are considered uniques values for each entity. (This seems not be true in the given example)
- Every import will be removed the previous data and replaced with the new one.

## Extructure of the project

The project is developed following an hexagonal architecture. The main idea is to separate the domain logic from the infrastructure logic.
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

This is a typical configuration used for access to the database.

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.batch_size=100
spring.datasource.url=jdbc:postgresql://localhost:5432/database
spring.datasource.username=test
spring.datasource.password=test
```

### Import configuration

The first three properties are used to configure the rest client.
The host, the number of times to retry the request and the seconds between retries.
**orders.import.http.page-size** is used to define the size of the requests when importing orders.
and **orders.jpa.page-size** is used to define the size
of transaction when persisting orders.

```properties
orders.rest.url=https://kata-espublicotech.g3stiona.com
orders.rest.times.to.retry=3
orders.rest.seconds.between.retries=5
orders.import.http.page-size=500
orders.import.jpa.page-size=250
```

### Export configuration

The page size of the blocks of orders to be exported into the file.

```properties
orders.export.page-size=2500
```

## Call the application

Once the application is running, the import process can be called
using `GET /v1/import` and the export process can be called using
`GET /v1/export`. There are another endpoint `GET /v1/import-export` to call both processes at the same time.
These endpoints are not secured and can be called without any authentication.
All the endpoints returned a json response with the information of the process.

### Example of import response

```json
{
  "summary": {
    "country": {
      "Benin": 4821,
      ...
      "Dominica": 4841,
      "Indonesia": 4927
    },
    "itemType": {
      "Vegetables": 74840,
      ...
      "Cosmetics": 75056,
      "Beverages": 74937
    },
    "salesChannel": {
      "ONLINE": 449538,
      "OFFLINE": 449962
    },
    "region": {
      "Australia and Oceania": 72700,
      "Asia": 131461,
      ...
      "Sub-Saharan Africa": 233630
    },
    "priority": {
      "HIGH": 224519,
      "MEDIUM": 224718,
      "LOW": 225068,
      "CRITICAL": 225195
    }
  },
  "errors": [
    {
      "page": 1496,
      "message": "some error"
    }
  ]
}
```

### Example of export response

```json
{
  "path": "/tmp/orders.csv"
}
```

### Example of import and export

```json
{
  "importSummary": {
    "summary": {
      "country": {
        "Benin": 4821,
        ...
        "Dominica": 4841,
        "Indonesia": 4927
      },
      "itemType": {
        "Vegetables": 74840,
        ...
        "Cosmetics": 75056,
        "Beverages": 74937
      },
      "salesChannel": {
        "ONLINE": 449538,
        "OFFLINE": 449962
      },
      "region": {
        "Australia and Oceania": 72700,
        "Asia": 131461,
        ...
        "Sub-Saharan Africa": 233630
      },
      "priority": {
        "HIGH": 224519,
        "MEDIUM": 224718,
        "LOW": 225068,
        "CRITICAL": 225195
      }
    },
    "errors": [
      {
        "page": 1496,
        "message": "some error"
      }
    ]
  },
  "exportSummary": {
    "path": "/tmp/orders.csv"
  }
}
```
