package me.renedo.espublico.orders.infraestructure.rest;

import static java.util.Optional.ofNullable;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class HttpOrderRepository {

    private final WebClient webClient;

    public HttpOrderRepository(@Value("${orders.rest.url}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url).build();
    }

    public PageDTO getPage(String url) {
        return ofNullable(webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(PageDTO.class).block())
                .map(ResponseEntity::getBody)
                .orElseThrow(() -> new RuntimeException("Not body"));
    }

    public PageDTO getPage(int page, int size) {
        return getPage("/v1/orders?page=" + page + "&max-per-page=" + size);
    }


    public record OrderDTO(String uuid, Long id, String region, String country,
                           @JsonProperty("item_type") String itemType, @JsonProperty("sales_channel") String salesChannel,
                           String priority, String date,
                           @JsonProperty("ship_date") String shipDate, @JsonProperty("units_sold") Integer unitsSold,
                           @JsonProperty("unit_price") Double unitPrice, @JsonProperty("unit_cost") Double unitCost,
                           @JsonProperty("total_revenue") Double totalRevenue, @JsonProperty("total_cost") Double totalCost,
                           @JsonProperty("total_profit") Double totalProfit) {
    }

    public record LinksDTO(String self, String next, String previous) {
    }

    public record PageDTO(int page, Set<OrderDTO> content, LinksDTO links) {
    }
}
