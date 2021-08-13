package com.app.task;

import com.app.task.Repository.CityRepo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataEnricherService {

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private com.app.task.Producer producer;

    public  JsonObject addData(String message) throws IOException {

        JsonObject input  = new JsonParser().parse(message).getAsJsonObject();
        String city = input.get("city").getAsString();
        String country = cityRepo.checkCountry(city);
        input.addProperty("country",country);
        JsonObject demography = enrich(input);
        send(demography);
        return input;
    }

    private JsonObject enrich (JsonObject demography) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String city = demography.get("city").getAsString();
        HttpGet request = new HttpGet("http://localhost:8089/demography/"+city);
        request.addHeader("Accept", "text/html");
        HttpResponse httpResponse = httpClient.execute(request);
        JsonObject responseJson = new JsonParser().parse(httpResponse.toString()).getAsJsonObject();
        String population  = responseJson.get("populaion").getAsString();
        demography.addProperty("population",population);

        return demography;
    }

    private void send(JsonObject demography){
        producer.sendMessage("output_topic", demography.toString());
    }
}
