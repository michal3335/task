package com.app.task;

import com.app.task.Model.Demography;
import com.app.task.Repository.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TaskApplication {

	@Autowired
	private CityRepo cityRepo;

	public static void main(String[] args) {

		SpringApplication.run(TaskApplication.class, args);


	}
	@EventListener(ApplicationReadyEvent.class)
	public void fillDB() {

		cityRepo.save(new Demography("Germany","Berlin"));

		cityRepo.save(new Demography("Poland","Warsaw"));

	}

}
