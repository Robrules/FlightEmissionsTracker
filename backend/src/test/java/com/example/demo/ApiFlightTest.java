package com.example.demo;

import com.example.demo.controller.FlightController;
import com.example.demo.entity.Flight;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Objects;

@SpringBootTest
public class ApiFlightTest {

    @Autowired
    FlightController flightController;

    @Test
    void getAllFlights() {
        ResponseEntity<Iterable<Flight>> flightResponseEntity;

        // Test that flights are returned when requested
        flightResponseEntity = flightController.getAllFlights();
        Assert.isTrue(flightResponseEntity.getStatusCode().equals(HttpStatus.OK),
                "Bad request received when requesting all flights");
        Assert.isTrue(!Objects.isNull(flightResponseEntity.getBody()), "All flights request returned null");
        Assert.isTrue(Lists.newArrayList(flightResponseEntity.getBody()).size() > 0,
                "All flights request returned empty");
    }

    @Test
    void getNullFilteredFlights() {
        ResponseEntity<Iterable<Flight>> flightResponseEntity;

        // Test when null inputs are given
        flightResponseEntity = flightController.filterFlights(null, null);
        Assert.isTrue(flightResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST),
                "Bad request not received when filtering flight entered as null, null");

    }

    @Test
    void getComboNullFilteredFlights() {
        ResponseEntity<Iterable<Flight>> flightResponseEntity;

        // Test when null input is given
        flightResponseEntity = flightController.filterFlights("CAN", null);
        Assert.isTrue(flightResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST),
                "Bad request not received when filtering flight entered as CAN, null");

        // Test when null input is given
        flightResponseEntity = flightController.filterFlights(null, "CGO");
        Assert.isTrue(flightResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST),
                "Bad request not received when filtering flight entered as null, CGO");
    }

    @Test
    void getGoodFilteredFlights() {
        ResponseEntity<Iterable<Flight>> flightResponseEntity;

        // Test when good input is given
        flightResponseEntity = flightController.filterFlights("CAN", "CGO");
        Assert.isTrue(flightResponseEntity.getStatusCode().equals(HttpStatus.OK),
                "Bad request received when filtering flight entered as CAN, CGO");
        Assert.isTrue(!Objects.isNull(flightResponseEntity.getBody()), "Filtered flights request returned null");
        Assert.isTrue(Lists.newArrayList(flightResponseEntity.getBody()).size() > 0,
                "Filtered flights request returned empty with CAN, CGO");
        Assert.isTrue(
                Lists.newArrayList(flightResponseEntity.getBody()).get(0).getSource().getCity().equals("Guangzhou"),
                "Filtered flights request returned incorrect source city with CAN, CGO");
        Assert.isTrue(Lists.newArrayList(flightResponseEntity.getBody()).get(0).getDestination().getCity()
                .equals("Zhengzhou"), "Filtered flights request returned incorrect dest city with CAN, CGO");
    }
}
