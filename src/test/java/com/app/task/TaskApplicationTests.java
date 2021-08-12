package com.app.task;
import com.app.task.Repository.CityRepo;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
class EmbeddedKafkaIntegrationTest {

	@Autowired
	private Consumer consumer;

	@Autowired
	private Producer producer;

	@Value("cities")
	private String topic;

	@Autowired
	private CityRepo cityRepo;

	@Test
	public void KafkaTest()
			throws Exception {

		JsonObject input = new JsonObject();
		input.addProperty("city","Berlin");

		producer.sendMessage("input_topic",input.toString());
		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

	}

	@Test
	public void DatabaseTest(){
		System.out.println(cityRepo.findById((long) 1));
	}
}