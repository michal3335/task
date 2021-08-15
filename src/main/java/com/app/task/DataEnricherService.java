package com.app.task;

import com.app.task.DTO.DemographyDTO;
import com.app.task.Repository.DemographyRepo;
import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@Service
public class DataEnricherService {


    private DemographyRepo demographyRepo;
    private DemographyProducer producer;

    @Autowired
    public DataEnricherService(DemographyRepo demographyRepo, DemographyProducer producer) {
        this.demographyRepo = demographyRepo;
        this.producer = producer;
    }

    public void addData(String message) throws IOException {
        JsonObject input  = new JsonParser().parse(message).getAsJsonObject();
        String city = input.get("city").getAsString();
        String country = demographyRepo.checkCountry(city);
        input.addProperty("country",country);
        JsonObject demography = enrich(input);
        send(demography);

    }

    private JsonObject enrich (JsonObject demography) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String city = demography.get("city").getAsString();
        HttpGet request = new HttpGet("http://localhost:8089/demography/"+city);
        request.addHeader("Accept", "application/json");
        HttpResponse httpResponse = httpClient.execute(request);
        String responseString = convertResponseToString(httpResponse);
        JsonObject populationJson = new JsonParser().parse(responseString).getAsJsonObject();
        demography.add("population",populationJson.get("population"));

        return demography;
    }

    private void send(JsonObject demography){
        Gson gson = new Gson();
        DemographyDTO demographyToSend = gson.fromJson(demography,DemographyDTO.class);
        producer.sendMessage("output_topic", demographyToSend.toString());
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();

        return responseString;
    }
}
