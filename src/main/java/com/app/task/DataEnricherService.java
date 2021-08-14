package com.app.task;

import com.app.task.Repository.CityRepo;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Scanner;

@Service
public class DataEnricherService {

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private com.app.task.Producer producer;

    public void addData(String message) throws IOException {

        JsonObject input  = new JsonParser().parse(message).getAsJsonObject();
        String city = input.get("city").getAsString();
        String country = cityRepo.checkCountry(city);
        input.addProperty("country",country);
        JsonObject demography = enrich(input);
        send(demography);

    }

    private JsonObject enrich (JsonObject demography) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String city = demography.get("city").getAsString();
        HttpGet request = new HttpGet("http://localhost:8089/demography/berlin");
        request.addHeader("Accept", "application/json");
        HttpResponse httpResponse = httpClient.execute(request);
        String responseString = convertResponseToString(httpResponse);
        JsonObject populationJson = new JsonParser().parse(responseString).getAsJsonObject();
        demography.add("population",populationJson.get("population"));

        return demography;
    }

    private void send(JsonObject demography){
        producer.sendMessage("output_topic", demography.toString());
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }
}
