package com.app.task;

import com.app.task.Model.Demography;
import com.app.task.Repository.DemographyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddDataToDatabase {

    @Autowired
    private DemographyRepo demographyRepo;


    public void fillDB() {
        demographyRepo.save(new Demography("GERMANY","BERLIN"));
        demographyRepo.save(new Demography("POLAND","WARSAW"));
    }
}
