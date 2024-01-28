package me.renedo.espublico.orders.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;

import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderRepository;
import me.renedo.espublico.orders.domain.PageOfOrders;
import me.renedo.espublico.orders.domain.PageOfOrdersRepository;

public class ImportUseCase {

    private final PageOfOrdersRepository pageOfOrdersRepository;

    private final OrderRepository orderRepository;

    private final int httpPageSize;

    private final int jpaPageSize;

    public ImportUseCase(PageOfOrdersRepository pageOfOrdersRepository, OrderRepository orderRepository,
            @Value("${orders.http.page-size}") int httpPageSize, @Value("${orders.jpa.page-size}") int jpaPageSize) {
        this.pageOfOrdersRepository = pageOfOrdersRepository;
        this.orderRepository = orderRepository;
        this.httpPageSize = httpPageSize;
        this.jpaPageSize = jpaPageSize;
    }

    public void execute() {
        truncateTables();
        PageOfOrders page = pageOfOrdersRepository.getFirstPage(httpPageSize);
        processOrders(page);
        while (page.getNextUrl() != null) {
            page = pageOfOrdersRepository.getPage(page.getNextUrl());
            processOrders(page);
        }
    }

    private Set<List<Order>> chopOrdersInPages(List<Order> list) {
        return IntStream.iterate(0, i -> i + jpaPageSize)
                .limit((list.size() + jpaPageSize - 1) / jpaPageSize)
                .boxed()
                .map(i -> list.subList(i, Math.min(i + jpaPageSize, list.size())))
                .collect(Collectors.toSet());
    }

    private void processOrders(PageOfOrders page) {
        chopOrdersInPages(page.getOrders()).forEach(orderRepository::saveAll);
    }

    private void truncateTables() {
        //TODO truncate tables
    }
}
