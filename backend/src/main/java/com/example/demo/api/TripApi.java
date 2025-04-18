package com.example.demo.api;

import com.example.demo.EmissionsCalculator;
import com.example.demo.entity.Flight;
import com.example.demo.repository.FlightRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Configurable
public class TripApi {

    private static Double yearAdjustedEmission(Double emissionsEstimate, Integer aircraftManufactureYear) {
        // return estimated emissions adjusted for
        // year of manufacture of aircraft

        // 3.4 is taken from the analysis done by Mckinsey and company
        // check frontend README for details
        Double rate = 3.4;
        Double adjustedEmission = emissionsEstimate;
        // Mckinsey analysis was done in 2020
        // no data to suggest 3.4% compound decrease
        // continued from 2020 to 2022
        Integer years = 2020 - aircraftManufactureYear;

        for (Integer i = 0; i < years; i++) {
            adjustedEmission += adjustedEmission * (rate / 100.0);
        }

        return adjustedEmission;
    }

    public String getTrips(JSONArray jsonArray, FlightRepository flightRepository) {
        // Extract individual trips from JSONObject
        // For each trip hit Climatiq API

        Double[] emissions = new Double[jsonArray.size()];
        EmissionsCalculator emissionsCalculator = new EmissionsCalculator();

        JSONObject returnJSON = new JSONObject();
        JSONArray legEmissions = new JSONArray();

        int i = 0;
        boolean success = true;
        for (Object obj : jsonArray) {
            JSONObject flightLeg = (JSONObject) obj;

            String from = (String) flightLeg.get("from");
            String to = (String) flightLeg.get("to");
            String flightClass = (String) flightLeg.get("class");
            String flightNo = (String) flightLeg.get("flightNo");
            Flight flight = flightRepository.findByFlightNo(flightNo);
            Integer passengers = flight.getAircraft().getCapacity();
            Integer manufactureYear = flight.getAircraft().getYearOfManufacture();

            JSONObject tempLeg = new JSONObject();

            if (from == null || from.isEmpty() || to == null || to.isEmpty() || passengers == 0 || flightClass == null
                    || flightClass.isEmpty()) {
                tempLeg.put("error", "Api request given in incorrect format");
                success = false;
            } else {
                try {
                    JSONParser parser = new JSONParser();
                    String apiResponse = emissionsCalculator.getEmissionsAPI(from, to, passengers, flightClass);
                    JSONObject json = (JSONObject) parser.parse(apiResponse);

                    JSONArray tempArray = (JSONArray) json.get("legs");
                    if (tempArray != null) {
                        JSONObject tempReturn = (JSONObject) tempArray.get(0);
                        emissions[i] = TripApi.yearAdjustedEmission((Double) tempReturn.get("co2e"), manufactureYear);
                    } else {
                        tempLeg.put("error", json.get("message"));
                        success = false;
                    }

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            tempLeg.put("from", flightLeg.get("from"));
            tempLeg.put("to", flightLeg.get("to"));
            tempLeg.put("passengers", passengers);
            tempLeg.put("class", flightLeg.get("class"));
            tempLeg.put("co2e", emissions[i]);

            legEmissions.add(tempLeg);

            i += 1;
        }

        if (success) {
            returnJSON.put("code", 200);
        } else {
            returnJSON.put("code", 400);
        }

        returnJSON.put("legs", legEmissions);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(returnJSON);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRecommendations(JSONArray jsonArray, FlightRepository flightRepository) {
        // Extract individual trips from JSONObject
        // Find each the flight numbers for the flights between the source and dest for each trip
        // Find the 3 aircraft model with the smallest capacity, corresponding to each flight
        // Return 3 flight numbers with the lowest total aircraft capacity

        JSONObject returnJSON = new JSONObject();
        JSONArray legRecommendations = new JSONArray();

        boolean success = true;

        for (Object obj : jsonArray) {
            JSONObject flightLeg = (JSONObject) obj;

            String source_code, dest_code;

            JSONObject tempLeg = new JSONObject();
            source_code = (String) flightLeg.get("from");
            dest_code = (String) flightLeg.get("to");

            try {
                Iterable<Flight> matchingFlights = flightRepository.findAllBySourceCodeAndDestinationCode(source_code,
                        dest_code);

                int i = 0;
                if (matchingFlights instanceof Collection) {
                    i = ((Collection<?>) matchingFlights).size();
                }

                System.out.printf("Found flights %s %s %d\n", source_code, dest_code, i);

                if (i == 0) {
                    success = false;
                    tempLeg.put("error", "No airport found with code given.");
                    tempLeg.put("flights_found", false);
                    tempLeg.put("from", source_code);
                    tempLeg.put("to", dest_code);
                    tempLeg.put("recommendations", "No flights found");

                    legRecommendations.add(tempLeg);
                } else {

                    String[][] aircraftFlightIds = new String[i][];

                    i = 0;
                    for (Flight matchingFlight : matchingFlights) {
                        aircraftFlightIds[i] = new String[] {
                                String.valueOf(matchingFlight.getAircraft().getCapacity()),
                                matchingFlight.getFlightNo() };
                        i += 1;
                    }

                    Arrays.sort(aircraftFlightIds, Comparator.comparingDouble(a -> Double.parseDouble(a[0])));
                    List<String> topThree = new ArrayList<>();

                    tempLeg.put("from", source_code);
                    tempLeg.put("to", dest_code);

                    topThree.add(aircraftFlightIds[0][1]);
                    if (aircraftFlightIds.length >= 2) {
                        topThree.add(aircraftFlightIds[1][1]);
                        if (aircraftFlightIds.length >= 3) {
                            topThree.add(aircraftFlightIds[2][1]);
                        }
                    }
                    tempLeg.put("recommendations", topThree.toArray());
                    tempLeg.put("flights_found", true);

                    legRecommendations.add(tempLeg);
                }

            } catch (ClassCastException e) {
                throw new RuntimeException(e);
            }

        }

        if (success) {
            returnJSON.put("code", 200);
        } else {
            returnJSON.put("code", 400);
        }

        returnJSON.put("legs", legRecommendations);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(returnJSON);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
