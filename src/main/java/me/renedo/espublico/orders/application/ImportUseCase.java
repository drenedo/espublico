package me.renedo.espublico.orders.application;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderRepository;
import me.renedo.espublico.orders.domain.PageOfOrders;
import me.renedo.espublico.orders.domain.PageOfOrdersRepository;
import me.renedo.espublico.orders.instrumentation.ImportInstrumentation;

@Component
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

    public ImportSummary execute() {
        truncateTables();
        ImportInstrumentation importInstrumentation = ImportInstrumentation.of(httpPageSize);
        PageOfOrders page = pageOfOrdersRepository.getFirstPage(httpPageSize);
        ImportSummary summary = processOrders(page, importInstrumentation);
        while (page.getNextUrl() != null) {
            page = pageOfOrdersRepository.getPage(page.getNextUrl());
            summary = summary.merge(processOrders(page, importInstrumentation));
        }
        importInstrumentation.finish();
        return summary;
    }

    private static Map<String, Integer> mapOfOne(String value) {
        return Map.of(value, 1);
    }

    private ImportSummary processOrders(PageOfOrders page, ImportInstrumentation importInstrumentation) {
        try {
            ImportSummary summary = page.getOrders().stream()
                    .map(ImportUseCase::toImportSummary)
                    .reduce(ImportSummary::merge).orElseGet(() -> new ImportSummary(Map.of(), List.of()));
            chopOrdersInPages(page.getOrders()).forEach(orderRepository::saveAll);
            importInstrumentation.registerPageInformation(page);
            return summary;
        } catch (Exception e) {
            importInstrumentation.registerError(e);
            return new ImportSummary(Map.of(), List.of(new ImportSummary.Error(page.getPage(), e.getMessage())));
        }
    }

    private static ImportSummary toImportSummary(Order order) {
        return new ImportSummary(Map.of("region", mapOfOne(order.getRegion().getName()),
                "country", mapOfOne(order.getCountry().getName()),
                "itemType", mapOfOne(order.getItemType().getName()),
                "salesChannel", mapOfOne(order.getSalesChannel().toString()),
                "priority", mapOfOne(order.getPriority().toString())), List.of());
    }

    private Set<List<Order>> chopOrdersInPages(List<Order> list) {
        return IntStream.iterate(0, i -> i + jpaPageSize)
                .limit((list.size() + jpaPageSize - 1) / jpaPageSize)
                .boxed()
                .map(i -> list.subList(i, Math.min(i + jpaPageSize, list.size())))
                .collect(Collectors.toSet());
    }

    private void truncateTables() {
        orderRepository.truncate();
    }
}
