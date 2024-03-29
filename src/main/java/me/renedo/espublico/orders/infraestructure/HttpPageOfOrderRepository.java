package me.renedo.espublico.orders.infraestructure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

import me.renedo.espublico.orders.domain.Country;
import me.renedo.espublico.orders.domain.ItemType;
import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderId;
import me.renedo.espublico.orders.domain.PageOfOrders;
import me.renedo.espublico.orders.domain.PageOfOrdersRepository;
import me.renedo.espublico.orders.domain.Priority;
import me.renedo.espublico.orders.domain.Region;
import me.renedo.espublico.orders.domain.SalesChannel;
import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository;
import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository.PageDTO;

@Component
public class HttpPageOfOrderRepository implements PageOfOrdersRepository {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    private final HttpOrderRepository httpOrderRepository;

    private final JpaRegionRepository regionRepository;

    private final JpaCountryRepository countryRepository;

    private final JpaItemTypeRepository itemTypeRepository;

    public HttpPageOfOrderRepository(HttpOrderRepository httpOrderRepository, JpaRegionRepository regionRepository,
            JpaCountryRepository countryRepository,
            JpaItemTypeRepository itemTypeRepository) {
        this.httpOrderRepository = httpOrderRepository;
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
        this.itemTypeRepository = itemTypeRepository;
    }

    @Override
    public PageOfOrders getFirstPage(int size) {
        return toPageOfOrders(httpOrderRepository.getPage(1, size));
    }

    @Override
    public PageOfOrders getPage(String url) {
        return toPageOfOrders(httpOrderRepository.getPage(url));
    }

    private Country toCountry(String countryName) {
        return countryRepository.findByNameOrCreate(countryName);
    }

    private ItemType toItemType(String itemTypeName) {
        return itemTypeRepository.findByNameOrCreate(itemTypeName);
    }

    private LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER);
    }

    private PageOfOrders toPageOfOrders(PageDTO page) {
        return new PageOfOrders(page.page(), page.content().stream()
                .map(order -> new Order(UUID.fromString(order.uuid()), new OrderId(order.id()), toRegion(order.region()),
                        toCountry(order.country()), toItemType(order.itemType()), toSalesChannel(order.salesChannel()),
                        toPriority(order.priority()), toLocalDate(order.date()), toLocalDate(order.shipDate()),
                        order.unitsSold(), BigDecimal.valueOf(order.unitPrice()), BigDecimal.valueOf(order.unitCost()),
                        BigDecimal.valueOf(order.totalRevenue()), BigDecimal.valueOf(order.totalCost()),
                        BigDecimal.valueOf(order.totalProfit())))
                .toList(), page.links().next());
    }

    private static Priority toPriority(String priority) {
        return switch (priority) {
            case "H" -> Priority.HIGH;
            case "M" -> Priority.MEDIUM;
            case "L" -> Priority.LOW;
            case "C" -> Priority.CRITICAL;
            // If there is an error in the data, we will ignore it at this point and will be evident in persistence phase
            default -> null;
        };
    }

    private Region toRegion(String regionName) {
        return regionRepository.findByNameOrCreate(regionName);
    }

    private static SalesChannel toSalesChannel(String salesChannel) {
        return switch (salesChannel) {
            case "Offline" -> SalesChannel.OFFLINE;
            case "Online" -> SalesChannel.ONLINE;
            // If there is an error in the data, we will ignore it at this point and will be evident in persistence phase
            default -> null;
        };
    }
}
