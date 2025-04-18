package com.example.demo.controller;

import com.example.demo.entity.Flight;
import com.example.demo.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/flight")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping(path = "/allFlights")
    public @ResponseBody ResponseEntity<Iterable<Flight>> getAllFlights() {
        try {
            Iterable<Flight> response = flightRepository.findAll();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/filterFlights")
    public @ResponseBody ResponseEntity<Iterable<Flight>> filterFlights(@RequestParam String from,
            @RequestParam String to) {
        if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            Iterable<Flight> response = flightRepository.findAllBySourceCodeAndDestinationCode(from, to);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
