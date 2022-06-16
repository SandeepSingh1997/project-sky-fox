# Setup service

## Install java
- Install jdk11
- Verify installed java version by running command
```shell script
java -version
javac -version
```

## Install colima to run containers
- Set up lima for docker
    - `brew install colima`
    - start colima by executing `colima start`
    - In the terminal type `docker ps` and you shouldn't see any error and possibly line indicating headers like "CONTAINER ID, IMAGE etc"


## Install postgres
- Setup Postgres using docker
   - Start db command
   `docker run --name postgresdb -e POSTGRES_DB=bookingengine -e POSTGRES_USER=bookingengine -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres`
   - Install below tools to connect to the database
   ```
      brew install --cask pgadmin4   
      brew install psqlodbc
   ```
  - Connect to db command
  `psql -h localhost -U bookingengine -d bookingengine`
    
## Seed data
- set DB_PASSWORD, POSTGRES_USERNAME, POSTGRES_DB as environment variables before running the script
   - To see values set for environment variable use
        Echo $POSTGRES_DB
   - Alternatively add the export commands to an env file and run ‘source env’
   ```
   export POSTGRES_DB=bookingengine
   export POSTGRES_USER=bookingengine
   export DB_PASSWORD=postgres
   ```
- params
    - starting date
    - number of weeks to seed data from given starting date

`sh seedShowData.sh 2022-05-31 3`

## Run the application
- Build the application and run the test along with coverage
```shell script
./gradlew clean
./gradlew build
```
- Run the server locally on localhost:8080
```shell script
./gradlew bootRun
```

## Run the application using docker compose
- Build the application and run the test along with coverage
```shell script
./gradlew clean
./gradlew build
```
- Run the server locally on localhost:8080
```shell script
docker-compose -f docker-compose-local.yml up 
```

## Swagger - API description format
- Server runs on port 8080(locally), the url to swagger is as shown below,
  http://localhost:8080/swagger-ui/index.html