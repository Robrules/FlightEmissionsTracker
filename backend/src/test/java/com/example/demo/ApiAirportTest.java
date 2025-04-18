package com.example.demo;

import com.example.demo.controller.AirportController;
import com.example.demo.entity.Airport;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Objects;

@SpringBootTest
public class ApiAirportTest {

    @Autowired
    AirportController airportController;

    @Test
    void getAllAirports() {
        ResponseEntity<Iterable<Airport>> airportResponseEntity;

        // Test that a list of airports is returned
        airportResponseEntity = airportController.getAllAirports();
        Assert.isTrue(airportResponseEntity.getStatusCode().equals(HttpStatus.OK),
                "Bad request received when requesting all airports");
        Assert.isTrue(!Objects.isNull(airportResponseEntity.getBody()), "All airport request returned null");
        Assert.isTrue(Lists.newArrayList(airportResponseEntity.getBody()).size() > 0,
                "All airport request returned empty");
    }
}
