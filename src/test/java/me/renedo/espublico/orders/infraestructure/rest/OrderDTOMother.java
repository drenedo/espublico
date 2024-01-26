package me.renedo.espublico.orders.infraestructure.rest;

import java.security.SecureRandom;
import java.util.UUID;

import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository.OrderDTO;

public class OrderDTOMother {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static OrderDTO any() {
        return new OrderDTO(UUID.randomUUID().toString(), RANDOM.nextLong(), "any-region", "any-country", "any-item-type",
                "Offline", "C", "01/01/2024", "01/01/2024", RANDOM.nextInt(), RANDOM.nextDouble(),
                RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble());
    }

    public static OrderDTO any(String uuid, Long id, String region, String country,
            String itemType, String salesChannel, String priority, String date,
            String shipDate, Integer unitsSold, Double unitPrice, Double unitCost,
            Double totalRevenue, Double totalCost, Double totalProfit) {
        return new OrderDTO(uuid, id, region, country, itemType, salesChannel, priority, date, shipDate, unitsSold, unitPrice,
                unitCost, totalRevenue, totalCost, totalProfit);
    }

    public static OrderDTO anyWithPriority(String priority) {
        return new OrderDTO(UUID.randomUUID().toString(), RANDOM.nextLong(), "any-region", "any-country", "any-item-type",
                "Offline", priority, "01/01/2024", "01/01/2024", RANDOM.nextInt(), RANDOM.nextDouble(),
                RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble());
    }

    public static OrderDTO anyWithSalesChanel(String salesChannel) {
        return new OrderDTO(UUID.randomUUID().toString(), RANDOM.nextLong(), "any-region", "any-country", "any-item-type",
                salesChannel, "C", "01/01/2024", "01/01/2024", RANDOM.nextInt(), RANDOM.nextDouble(),
                RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble());
    }
}
