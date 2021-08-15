# DevTask
The main method for testing the application is called shouldEnrichDemographyDataWithCountryandPopulationAndSendToOutputTopic() and
it is located in EmbeddedKafkaIntegrationTest class in Test section.

 Application uses Embedded Kafka, there is a consumer for reading incoming messages  and producer for sending enriched messages.
When the message is received, it is passed to DataEnricherService which checks for matching country in H2 database than uses http request to get population from wiremock server and in the end uses send() method for parsing the data to DTO object and sending it with producer to kafka.
There is fakeConsumer in Test section which reads incoming messages and checks if they are equal with expected output.

Kafka server, WireMock server and rest stubs are created during Tests.
