package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class EmissionsCalculator {

    public String getEmissionsAPI(String source, String dest, int capacity, String flight_class) {
        String key = "";

        try {
            Scanner scanner = new Scanner(new File("src/main/resources/climatiqKey.pm"));
            while (scanner.hasNextLine()) {
                key = scanner.nextLine().trim();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String requestBody = "{\n" + "        \"legs\": [\n" + "            {\n" + "                \"from\": \""
                + source + "\",\n" + "                \"to\": \"" + dest + "\",\n" + "                \"passengers\": "
                + capacity + ",\n" + "                \"class\": \"" + flight_class + "\"\n" + "            }"
                + "        ]\n" + "    }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://beta3.api.climatiq.io/travel/flights"))
                .header("Authorization", "Bearer: " + key).POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }

}
