package com.app.task;

import com.app.task.Repository.CityRepo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataProcessing {

    @Autowired
    private CityRepo cityRepo;

    public  JsonObject addData(String message){

        JsonObject input  = new JsonParser().parse(message).getAsJsonObject();
        String city = input.get("city").getAsString();
        String country = cityRepo.checkCountry(city);
        input.addProperty("country",country);

        return input;
    }
}
