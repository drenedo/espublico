package me.renedo.espublico.orders.application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderRepository;
import me.renedo.espublico.orders.instrumentation.ExportInstrumentation;

@Component
public class ExportUseCase {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");
    private final static String[] CSV_HEADERS = {"Order ID", "Order Priority", "Order Date", "Region", "Country", "Item Type", "Sales Channel",
            "Ship Date", "Units Sold", "Unit Price", "Unit Cost", "Total Revenue", "Total Cost", "Total Profit"};

    private final OrderRepository orderRepository;

    private final int pageSize;

    public ExportUseCase(OrderRepository orderRepository, @Value("${orders.export.page-size}") int pageSize) {
        this.orderRepository = orderRepository;
        this.pageSize = pageSize;
    }

    public ExportInformation execute() {
        Path file = getTemporalFile();
        ExportInstrumentation instrumentation = ExportInstrumentation.of(pageSize);
        List<Order> orders = orderRepository.getPage(0, pageSize);
        appendOrdersToFile(file, orders, true);
        instrumentation.registerPageInformation(0, orders.size());
        int page = 1;
        while (orders.size() == pageSize) {
            orders = orderRepository.getPage(page, pageSize);
            appendOrdersToFile(file, orders, false);
            instrumentation.registerPageInformation(page, orders.size());
            page++;
        }
        instrumentation.finish();
        return new ExportInformation(file.toAbsolutePath().toString());
    }

    private static void appendOrdersToFile(Path path, List<Order> orders, boolean headers) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            CSVPrinter csvPrinter = new CSVPrinter(writer, headers ?
                    CSVFormat.Builder.create().setHeader(CSV_HEADERS).build() : CSVFormat.DEFAULT);
            orders.forEach(getOrderConsumer(csvPrinter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Consumer<Order> getOrderConsumer(CSVPrinter csvPrinter) {
        return o -> {
            try {
                csvPrinter.printRecord(o.getId().getValue(), o.getPriority().toString(), o.getDate().format(FORMATTER), o.getRegion().getName(),
                        o.getCountry().getName(), o.getItemType().getName(), o.getSalesChannel().toString(), o.getShipDate(), o.getUnitsSold(),
                        o.getUnitPrice(), o.getUnitCost(), o.getTotalRevenue(), o.getTotalCost(), o.getTotalProfit());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Path getTemporalFile() {
        try {
            return File.createTempFile("orders", ".csv").toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
