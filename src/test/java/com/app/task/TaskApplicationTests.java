package com.app.task;
import com.app.task.Repository.CityRepo;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.JsonObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(JUnit4.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
class EmbeddedKafkaIntegrationTest {

	@Autowired
	private DemographyConsumer demographyConsumer;

	@Autowired
	private Producer producer;

	@Autowired
	private CityRepo cityRepo;

	@Mock
	private KafkaTemplate kafkaTemplate;


	@Test
	public void shouldEnrichDemographyDataWithCountryandPopulationAndSendToOutputTopic()
			throws Exception {


		JsonObject input = new JsonObject();
		input.addProperty("city", "Berlin");

		producer.sendMessage("input_topic", input.toString());
		demographyConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

		//Mockito.verify(kafkaTemplate).send("input_topic",input.toString());


	}

	@Test
	public void DatabaseTest() {
		System.out.println(cityRepo.findById((long) 1));
	}


	@Before
	public void WiremockTest(){

		WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8089));


		wireMockServer.stubFor(get(urlPathMatching("/demography/berlin"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("\"population\": \"3645000\"")));

		wireMockServer.stubFor(get(urlPathMatching("/demography/warsaw"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("\"population\": \"1765000\"")));


		wireMockServer.start();

	}




}