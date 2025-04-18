package com.example.demo.repository;

import com.example.demo.entity.Airport;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called AirportRepository
// CRUD refers Create, Read, Update, Delete

public interface AirportRepository extends CrudRepository<Airport, Integer> {

    Iterable<Airport> findAll();

    Airport findByCode(String code);

}
