# ELEC5619_Project Backend

This is the backend service for our Flight Emissions Tracker.

## Pre-Requisites

You will need the following before you use this service:

* Java 11

* [Docker](https://docs.docker.com/get-docker/)

## How to Run

To run the backend service, first run the below terminal command in the current directory. This will spin up a docker
container for the MySQL database.

```docker compose up -d```

Then run either of the following commands:

* Mac - `./mvnw spring-boot:run`

* Windows - `mvnw spring-boot:run`

This will start the application, create the database schemas and populate the data based on the instructions found in `ELEC5619_Project/backend/src/main/resources/data.sql`.

## How to Build

To build the application run either of the following:

* Mac - `./mvnw clean package`

* Windows - `mvnw clean package`

Then run `java -jar target/demo-0.0.1-SNAPSHOT.jar` to run it.

## Curl commands

### FlightController

* `curl 'http://localhost:8080/flight/allFlights'` - Get all flights and related information

* `curl 'http://localhost:8080/flight/filterFlights?from=FROM&to=TO'` - Find all flights matching the source and
  destination provided.
  Try `curl 'http://localhost:8080/flight/filterFlights?from=GUR&to=POM'`

### AirportController

* `curl 'http://localhost:8080/airport/allAirports'` - Get all airports and related information

### TripController

* `curl -X POST -H "Content-Type: application/json" -d STRING 'http://localhost:8080/trip/carbonEmissions'` - Run an API to get carbon emission for given
  legs. Legs should be json strings but given as string. Get emissions calculations for the given flights  
  Try: `curl -X POST -H "Content-Type: application/json" -d "[{\"from\":\"SYD\",\"to\":\"MEL\",\"flightNo\":\"QFA425\",\"class\":\"economy\"}]" 'http://localhost:8080/trip/carbonEmissions'`

* `curl -X POST -H "Content-Type: application/json" -d STRING 'http://localhost:8080/trip/recommendations` - Run an API to get recommendations for given
  legs. Legs should be json strings but given as string. Get a set of recommendations for flights with an estimated low
  value of emissions.  
  Try: `curl -X POST -H "Content-Type: application/json" -d "[{\"from\":\"SYD\",\"to\":\"MEL\"}]" 'http://localhost:8080/trip/recommendations'`

## Acknowledgments

The fake data that will be inserted into the database is derived from the following sources:

* Information about aircraft, sourced from AirplanesDB API
* Information about airports, sourced from Flight Radar API
* Information about flights, sourced from Airlabs API

