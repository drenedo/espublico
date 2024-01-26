package me.renedo.espublico.orders.infraestructure.rest;

import java.security.SecureRandom;
import java.util.Set;

import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository.PageDTO;

public class PageDTOMother {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static PageDTO any() {
        return new PageDTO(RANDOM.nextInt(100), Set.of(OrderDTOMother.any()), me.renedo.espublico.orders.infraestructure.rest.LinksDTOMother.any());
    }

    public static PageDTO any(HttpOrderRepository.OrderDTO orderDTO) {
        return new PageDTO(RANDOM.nextInt(100), Set.of(orderDTO), LinksDTOMother.any());
    }
}
