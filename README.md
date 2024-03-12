# spring-data-jpa-multi-dbs-sample

Sample spring-boot app that shows configuring/accessing multiple databases through spring-data-jpa. The sample shows how to configure the DataSources, Hikari connection pools, and liquibase database schema changes as code.

## Start the databases

Use `docker compose up -d` command to start up the MySQL and PostgreSQL instances. Ports `3306` and `5432` are used by these databases.

## Run the app

The app listens on port `8080` and requires JDK 21 to compile/run. Compile/run it by:

```
./mvnw clean spring-boot:run
```

## Test

Go to http://localhost:8080, which will forward to a swagger page. The APIs can be exercised there.

## Shutdown the databases

Shutting down the databases will destroy all the data in them.

```
docker compose down
```