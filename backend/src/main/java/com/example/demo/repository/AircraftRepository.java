package com.example.demo.repository;

import com.example.demo.entity.Aircraft;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called aircraftRepository
// CRUD refers Create, Read, Update, Delete

public interface AircraftRepository extends CrudRepository<Aircraft, Integer> {

}