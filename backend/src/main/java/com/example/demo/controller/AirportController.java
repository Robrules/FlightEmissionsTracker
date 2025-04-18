package com.example.demo.controller;

import com.example.demo.entity.Airport;
import com.example.demo.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/airport")
@CrossOrigin(origins = "http://localhost:3000")
public class AirportController {

    @Autowired
    private AirportRepository airportRepository;

    @GetMapping(value = "/allAirports")
    @ResponseBody
    public ResponseEntity<Iterable<Airport>> getAllAirports() {
        try {
            Iterable<Airport> response = airportRepository.findAll();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
