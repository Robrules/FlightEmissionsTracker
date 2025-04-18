package com.example.demo.repository;

import com.example.demo.entity.Flight;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called FlightRepository
// CRUD refers Create, Read, Update, Delete

public interface FlightRepository extends CrudRepository<Flight, Integer> {

    Iterable<Flight> findAllBySourceCodeAndDestinationCode(String source, String destination);

    Flight findByFlightNo(String flightNo);
}
