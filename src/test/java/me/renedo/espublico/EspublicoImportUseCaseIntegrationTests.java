package me.renedo.espublico;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import me.renedo.espublico.orders.application.ImportSummary;
import me.renedo.espublico.orders.application.ImportUseCase;
import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.Priority;
import me.renedo.espublico.orders.domain.SalesChannel;
import me.renedo.espublico.orders.infraestructure.JpaOrderRepository;

@SpringBootTest
@Import(TestEspublicoApplication.class)
@WireMockTest(httpPort = 8089)
class EspublicoImportUseCaseIntegrationTests {

    @Autowired
    private ImportUseCase useCase;

    @Autowired
    private JpaOrderRepository repository;

    private void assertResponseAndData(ImportSummary summary) {
        assertThat(summary.getErrors()).hasSize(0);
        assertThat(summary.getSummary().get("region").get("Sub-Saharan Africa")).isEqualTo(75);
        List<Order> orders = repository.getPage(0, 500);
        assertThat(orders).hasSize(290);
        Optional<Order> matched =
                orders.stream().filter(order -> order.getUuid().toString().equals("958621cc-2530-4068-9b9a-ecc028bdde02")).findFirst();
        assertThat(matched).isNotEmpty();
        assertThat(matched.get().getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(matched.get().getSalesChannel()).isEqualTo(SalesChannel.ONLINE);
        assertThat(matched.get().getShipDate()).isEqualTo(LocalDate.of(2010, 5, 6));
        assertThat(matched.get().getDate()).isEqualTo(LocalDate.of(2010, 4, 4));
        assertThat(matched.get().getUnitsSold()).isEqualTo(7984);
        assertThat(matched.get().getUnitCost().doubleValue()).isEqualTo(56.67);
        assertThat(matched.get().getTotalRevenue().doubleValue()).isEqualTo(652532.32);
        assertThat(matched.get().getTotalProfit().doubleValue()).isEqualTo(200079.04);
        assertThat(matched.get().getTotalCost().doubleValue()).isEqualTo(452453.28);
    }

    private static void mockUrls() {
        stubFor(get(urlEqualTo("/v1/orders?page=1&max-per-page=500")).inScenario("Test")
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response.json")));
        stubFor(get(urlEqualTo("/v1/orders?page=2&max-per-page=500")).inScenario("Test")
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response2.json")));
        stubFor(get(urlEqualTo("/v1/orders?page=3&max-per-page=500")).inScenario("Test")
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response3.json")));
    }

    private static void mockUrlsWithRetries() {
        stubFor(get(urlEqualTo("/v1/orders?page=1&max-per-page=500")).inScenario("Test")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response.json")));
        stubFor(get(urlEqualTo("/v1/orders?page=2&max-per-page=500")).inScenario("Test")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(500).withHeader("Content-Type", "application/json")
                        .withBody("{error: 'some error'}"))
                .willSetStateTo("One attempt"));
        stubFor(get(urlEqualTo("/v1/orders?page=2&max-per-page=500")).inScenario("Test")
                .whenScenarioStateIs("One attempt")
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response2.json")));
        stubFor(get(urlEqualTo("/v1/orders?page=3&max-per-page=500")).inScenario("Test")
                .whenScenarioStateIs("One attempt")
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response3.json")));
    }

    @Test
    void ensure_application_starts_with_a_database_without_errors() {
        // Given
        mockUrls();

        // When
        ImportSummary summary = useCase.execute();

        // Then
        assertResponseAndData(summary);
    }

    @Test
    void ensure_application_continues_on_errors() {
        // Given
        mockUrlsWithRetries();

        // When
        ImportSummary summary = useCase.execute();

        // Then
        assertResponseAndData(summary);
    }

    @AfterEach
    void clean_orders() {
        repository.truncate();
    }
}
