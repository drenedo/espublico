package me.renedo.espublico.orders.infraestructure.rest;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository.PageDTO;

@WireMockTest(httpPort = 8089)
class HttpOrderRepositoryTest {

    private final HttpOrderRepository httpOrderRepository = new HttpOrderRepository("http://127.0.0.1:8089");

    @Test
    void check_page_and_ensure_mapping_is_correct() {
        // Given
        mockUrl();

        // When
        PageDTO page = httpOrderRepository.getPage(1, 100);

        // Then
        assertPage(page);
    }

    @Test
    void check_page_by_url_and_ensure_mapping_is_correct() {
        // Given
        mockUrl();

        // When
        PageDTO page = httpOrderRepository.getPage("/v1/orders?page=1&max-per-page=100");

        // Then
        assertPage(page);
    }

    private static void assertPage(PageDTO page) {
        assertThat(page).isNotNull();
        assertThat(page.content()).hasSize(100);
        assertThat(page.content().stream()
                .filter(o -> o.uuid().equals("6360cfc4-8d3e-4862-87f3-7e7cc8be388a"))
                .findFirst().get()).usingRecursiveComparison()
                .isEqualTo(OrderDTOMother.any("6360cfc4-8d3e-4862-87f3-7e7cc8be388a", 862861335L, "Sub-Saharan Africa",
                        "The Gambia", "Fruits", "Online", "C", "11/20/2011", "11/22/2011", 8699, 9.33, 6.92, 81161.67, 60197.08,
                        20964.59));
    }

    private static void mockUrl() {
        stubFor(get(urlEqualTo("/v1/orders?page=1&max-per-page=100"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("response.json")));
    }
}
