package com.app.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class TaskApplication {

	private AddDataToDatabase addDataToDatabase;

	@Autowired
	public TaskApplication(AddDataToDatabase addDataToDatabase) {
		this.addDataToDatabase = addDataToDatabase;
	}

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	private void fillDatabase(){
		addDataToDatabase.fillDB();
	}


}
