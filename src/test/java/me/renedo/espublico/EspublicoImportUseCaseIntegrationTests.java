package me.renedo.espublico;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClientException;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
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

    private static void mockUrls() {
        stubFor(get(urlEqualTo("/v1/orders?page=1&max-per-page=500"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response.json")));
        stubFor(get(urlEqualTo("/v1/orders?page=2&max-per-page=500"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response2.json")));
        stubFor(get(urlEqualTo("/v1/orders?page=3&max-per-page=500"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response3.json")));
    }

    @Test
    void ensure_application_starts_with_a_database_without_errors() {
        // Given
        mockUrls();

        // When
        useCase.execute();

        // Then
        List<Order> orders = repository.getPage(0, 500);
        assertThat(orders).hasSize(290);
        Optional<Order> matched = orders.stream().filter(order -> order.getUuid().equals("958621cc-2530-4068-9b9a-ecc028bdde02")).findFirst();
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

    @Test
    void ensure_application_continue_on_errors() {
        // Given
        mockUrls();
        stubFor(get(urlEqualTo("/v1/orders?page=2&max-per-page=500"))
                .willReturn(aResponse().withStatus(500).withHeader("Content-Type", "application/json")
                        .withBody("{error: 'some error'}")));

        // When
        assertThrows(WebClientException.class, () -> useCase.execute());
        //TODO think about a retry mechanism in http client
    }
}
