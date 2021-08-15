package com.app.task;
import com.app.task.DTO.DemographyDTO;
import com.app.task.Repository.DemographyRepo;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@ActiveProfiles("integration")
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
class EmbeddedKafkaIntegrationTest {

	private DemographyConsumer demographyConsumer;

	private DemographyProducer producer;

	private DemographyRepo demographyRepo;

	@Autowired
	public EmbeddedKafkaIntegrationTest(DemographyConsumer demographyConsumer, DemographyProducer producer, DemographyRepo demographyRepo) {
		this.demographyConsumer = demographyConsumer;
		this.producer = producer;
		this.demographyRepo = demographyRepo;
	}

	@Test
	public void shouldEnrichDemographyDataWithCountryandPopulationAndSendToOutputTopic()
			throws Exception {

		WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8089));


		wireMockServer.stubFor(get(urlPathMatching("/demography/BERLIN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"population\": \"3645000\"}")));

		wireMockServer.stubFor(get(urlPathMatching("/demography/WARSAW"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"population\": \"1765000\"}")));


		wireMockServer.start();


		JsonObject input = new JsonObject();
		input.addProperty("city", "BERLIN");

		producer.sendMessage("input_topic", input.toString());
		demographyConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);


		wireMockServer.stop();

	}


	@Test
	public void DatabaseTest() {
		System.out.println(demographyRepo.findById((long) 1));
	}


}