package com.example.demo.controller;

import com.example.demo.api.TripApi;
import com.example.demo.repository.AircraftRepository;
import com.example.demo.repository.FlightRepository;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
@RequestMapping(path = "/trip")
public class TripController {

    private final TripApi tripApi = new TripApi();

    @Autowired
    private FlightRepository flightRepository;

    @PostMapping(value = "/carbonEmissions")
    @ResponseBody
    public ResponseEntity<String> calculateCarbonEmissions(@RequestBody String legs) {
        if (legs == null || legs.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            JSONParser parser = new JSONParser();
            JSONArray legsArr = (JSONArray) parser.parse(legs);

            String response = tripApi.getTrips(legsArr, flightRepository);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/recommendations")
    @ResponseBody
    public ResponseEntity<String> calculateFlightRecommendations(@RequestBody String legs) {
        if (legs == null || legs.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            JSONParser parser = new JSONParser();
            JSONArray legsArr = (JSONArray) parser.parse(legs);

            String response = tripApi.getRecommendations(legsArr, flightRepository);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
