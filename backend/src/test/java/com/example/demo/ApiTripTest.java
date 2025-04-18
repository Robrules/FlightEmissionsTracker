package com.example.demo;

import com.example.demo.controller.TripController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApiTripTest {

    @Autowired
    TripController tripController;

    @Test
    void getNullRecommendations() {
        ResponseEntity<String> flightRecommendations;

        // Test when null input is given
        flightRecommendations = tripController.calculateFlightRecommendations(null);
        Assert.isTrue(flightRecommendations.getStatusCode().equals(HttpStatus.BAD_REQUEST),
                "Bad request not received for null flight recommendation input");
    }

    @Test
    void getBadInputRecommendations() {
        ResponseEntity<String> flightRecommendations;

        JSONParser jsonParser = new JSONParser();
        try {
            // Test when bad input is given
            JSONObject jsonObject = (JSONObject) jsonParser.parse(
                    "{\n" + "  \"legs\": [\n" + "    {\n" + "      \"from\": \"CAN\",\n" + "    }" + "  ]\n" + "}");
            flightRecommendations = tripController
                    .calculateFlightRecommendations(((JSONArray) jsonObject.get("legs")).toJSONString());
            jsonObject = (JSONObject) jsonParser.parse(flightRecommendations.getBody());
            Assert.isTrue(((Long) jsonObject.get("code")) == 400,
                    "Wrong status code returned for incorrect json flight recommendations request");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBadInputSingleLegRecommendations() {
        ResponseEntity<String> flightRecommendations;
        JSONParser jsonParser = new JSONParser();
        JSONArray requestLeg = null;

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse("{\n" + "  \"legs\": [\n" + "    {\n"
                    + "      \"from\": \"CAN\",\n" + "      \"to\": \"CGO\",\n" + "      \"passengers\": 2,\n"
                    + "      \"class\": \"first\"\n" + "    },\n" + "    {\n" + "      \"from\": \"CKG\",\n"
                    + "      \"to\": \"CAN\",\n" + "      \"passengers\": 2,\n" + "      \"class\": \"economy\"\n"
                    + "    }, \n" + "    {\n" + "      \"from\": \"CAN\",\n" + "      \"to\": \"CAN\",\n"
                    + "      \"passengers\": 2,\n" + "      \"class\": \"economy\"\n" + "    }\n" + "  ]\n" + "}");
            requestLeg = (JSONArray) jsonObject.get("legs");
        } catch (Exception ex) {
            Assert.isTrue(false, "Request json could not be read from string");
        }

        // Test when bad input is given
        flightRecommendations = tripController.calculateFlightRecommendations(requestLeg.toJSONString());
        Assert.isTrue(flightRecommendations.getStatusCode().equals(HttpStatus.OK),
                "Bad request received for non null flight recommendations input");

        try {
            // Test when returned recommendations
            JSONObject recommendations = (JSONObject) jsonParser.parse(flightRecommendations.getBody());
            Assert.isTrue(((Long) recommendations.get("code")) == 400,
                    "Wrong status code returned for incorrect flight recommendations request");

            JSONArray recommendedLegs = (JSONArray) recommendations.get("legs");
            Assert.isTrue(((JSONObject) recommendedLegs.get(0)).get("flights_found").equals(true),
                    "Flights no found status returned for recommendations leg 1");

            Assert.isTrue(((JSONObject) recommendedLegs.get(1)).get("flights_found").equals(true),
                    "Flights no found status returned for recommendations leg 2");

            Assert.isTrue(((JSONObject) recommendedLegs.get(2)).get("flights_found").equals(false),
                    "Flights found status returned for recommendations leg 3 when no flights should exist");

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSameAirportsRecommendations() {
        ResponseEntity<String> flightRecommendations;
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(
                    "{\n" + "  \"legs\": [\n" + "    {\n" + "      \"from\": \"CAN\",\n" + "      \"to\": \"CAN\",\n"
                            + "      \"passengers\": 2,\n" + "      \"class\": \"first\"\n" + "    },\n" + "    {\n"
                            + "      \"from\": \"CKG\",\n" + "      \"to\": \"CAN\",\n" + "      \"passengers\": 2,\n"
                            + "      \"class\": \"economy\"\n" + "    }\n" + "  ]\n" + "}");
            flightRecommendations = tripController
                    .calculateFlightRecommendations(((JSONArray) jsonObject.get("legs")).toJSONString());
            jsonObject = (JSONObject) jsonParser.parse(flightRecommendations.getBody());
            Assert.isTrue(((Long) jsonObject.get("code")) == 400,
                    "Wrong status code returned for incorrect json flight emissions request");

            Assert.isTrue(((JSONObject) ((JSONArray) jsonObject.get("legs")).get(0)).get("flights_found").equals(false),
                    "Flights found status returned for recommendations leg 1");

            Assert.isTrue(((JSONObject) ((JSONArray) jsonObject.get("legs")).get(1)).get("flights_found").equals(true),
                    "Flights no found status returned for recommendations leg 2");

        } catch (Exception ex) {
            Assert.isTrue(false, "Request json could not be read from string");
        }
    }

    @Test
    void getGoodRecommendations() {
        ResponseEntity<String> flightRecommendations;
        JSONParser jsonParser = new JSONParser();
        JSONArray requestLeg = null;

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(
                    "{\n" + "  \"legs\": [\n" + "    {\n" + "      \"from\": \"CAN\",\n" + "      \"to\": \"CGO\",\n"
                            + "      \"passengers\": 2,\n" + "      \"class\": \"first\"\n" + "    },\n" + "    {\n"
                            + "      \"from\": \"CKG\",\n" + "      \"to\": \"CAN\",\n" + "      \"passengers\": 2,\n"
                            + "      \"class\": \"economy\"\n" + "    } \n" + "  ]\n" + "}");
            requestLeg = (JSONArray) jsonObject.get("legs");
        } catch (Exception ex) {
            Assert.isTrue(false, "Request json could not be read from string");
        }

        // Test when good input is given
        flightRecommendations = tripController.calculateFlightRecommendations(requestLeg.toJSONString());
        Assert.isTrue(flightRecommendations.getStatusCode().equals(HttpStatus.OK),
                "Bad request received for non null flight recommendations input");

        try {
            // Test when returned recommendations
            JSONObject recommendations = (JSONObject) jsonParser.parse(flightRecommendations.getBody());
            Assert.isTrue(((Long) recommendations.get("code")) == 200,
                    "Wrong status code returned for incorrect flight recommendations request");

            JSONArray recommendedLegs = (JSONArray) recommendations.get("legs");
            Assert.isTrue(((JSONObject) recommendedLegs.get(0)).get("flights_found").equals(true),
                    "Flights no found status returned for recommendations leg 1");

            Assert.isTrue(((JSONObject) recommendedLegs.get(1)).get("flights_found").equals(true),
                    "Flights no found status returned for recommendations leg 2");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getNullTrips() {
        ResponseEntity<String> flightRecommendations;

        // Test when null input is given
        flightRecommendations = tripController.calculateFlightRecommendations(null);
        Assert.isTrue(flightRecommendations.getStatusCode().equals(HttpStatus.BAD_REQUEST),
                "Bad request not received for null flight emissions input");
    }

    @Test
    void getBadTrips() {
        ResponseEntity<String> flightRecommendations;
        JSONParser jsonParser = new JSONParser();

        try {
            // Test when bad input is given
            JSONObject jsonObject = (JSONObject) jsonParser.parse(
                    "{\n" + "  \"legs\": [\n" + "    {\n" + "      \"from\": \"ATL\",\n" + "    }" + "  ]\n" + "}");
            flightRecommendations = tripController
                    .calculateFlightRecommendations(((JSONArray) jsonObject.get("legs")).toJSONString());
            jsonObject = (JSONObject) jsonParser.parse(flightRecommendations.getBody());
            Assert.isTrue(((Long) jsonObject.get("code")) == 400,
                    "Wrong status code returned for incorrect json flight emissions request");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getMockTrips() {
        ResponseEntity<String> flightRecommendations;
        JSONArray requestLeg = null;
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse("{\n" + "  \"legs\": [\n" + "    {\n"
                    + "      \"from\": \"CAN\",\n" + "      \"to\": \"CGO\",\n" + "      \"flightNo\": \"CNG123\",\n"
                    + "      \"class\": \"first\"\n" + "    },\n" + "    {\n" + "      \"from\": \"CKG\",\n"
                    + "      \"to\": \"CAN\",\n" + "      \"flightNo\": \"KGC098\",\n"
                    + "      \"class\": \"economy\"\n" + "    }\n" + "  ]\n" + "}");
            requestLeg = (JSONArray) jsonObject.get("legs");
        } catch (Exception ex) {
            Assert.isTrue(false, "Request json could not be read from string");
        }

        // Does not actually hit the api for testing
        TripController mockedTripController = mock(TripController.class);
        when(mockedTripController.calculateCarbonEmissions(requestLeg.toJSONString())).thenReturn(new ResponseEntity<>(
                "{\n" + "  \"code\": 200,\n" + "  \"legs\": [\n" + "    {\n" + "      \"co2e\": 1441.4876377092812,\n"
                        + "      \"passengers\": 2,\n" + "      \"from\": \"ATL\",\n" + "      \"to\": \"CDG\",\n"
                        + "      \"class\": \"first\"\n" + "    },\n" + "    {\n" + "      \"co2e\": 234.61658862656,\n"
                        + "      \"passengers\": 2,\n" + "      \"from\": \"BLR\",\n" + "      \"to\": \"BOM\",\n"
                        + "      \"class\": \"economy\"\n" + "    }\n" + "  ]\n" + "}",
                HttpStatus.OK));

        // Test mock returns
        try {
            flightRecommendations = mockedTripController.calculateCarbonEmissions(requestLeg.toJSONString());
            JSONObject mockReturn = (JSONObject) jsonParser.parse(flightRecommendations.getBody());
            Assert.isTrue(((Long) mockReturn.get("code")) == 200, "Wrong status code read when mocking api return");
            Assert.isTrue(flightRecommendations.getStatusCode().equals(HttpStatus.OK),
                    "Bad request received when mocking api return");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
