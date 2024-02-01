package me.renedo.espublico.orders.infraestructure;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import me.renedo.espublico.orders.domain.Country;
import me.renedo.espublico.orders.domain.ItemType;
import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderId;
import me.renedo.espublico.orders.domain.OrderRepository;
import me.renedo.espublico.orders.domain.Priority;
import me.renedo.espublico.orders.domain.Region;
import me.renedo.espublico.orders.domain.SalesChannel;
import me.renedo.espublico.orders.infraestructure.jpa.CountryEntity;
import me.renedo.espublico.orders.infraestructure.jpa.ItemTypeEntity;
import me.renedo.espublico.orders.infraestructure.jpa.OrderEntity;
import me.renedo.espublico.orders.infraestructure.jpa.OrderEntityRepository;
import me.renedo.espublico.orders.infraestructure.jpa.RegionEntity;

@Component
public class JpaOrderRepository implements OrderRepository {

    private final OrderEntityRepository orderEntityRepository;

    private final JpaRegionRepository jpaRegionRepository;

    private final JpaCountryRepository jpaCountryRepository;

    private final JpaItemTypeRepository jpaItemTypeRepository;

    public JpaOrderRepository(OrderEntityRepository orderEntityRepository, JpaRegionRepository jpaRegionRepository,
            JpaCountryRepository jpaCountryRepository, JpaItemTypeRepository jpaItemTypeRepository) {
        this.orderEntityRepository = orderEntityRepository;
        this.jpaRegionRepository = jpaRegionRepository;
        this.jpaCountryRepository = jpaCountryRepository;
        this.jpaItemTypeRepository = jpaItemTypeRepository;
    }

    @Override
    public List<Order> getPage(int page, int size) {
        Page<OrderEntity> orders = orderEntityRepository.findAll(PageRequest.of(page, size, Sort.by("id")));
        return orders.getContent().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Country toCountry(Integer id) {
        return jpaCountryRepository.findById(id);
    }

    private Order toDomain(OrderEntity orderEntity) {
        return new Order(orderEntity.getUuid(), new OrderId(orderEntity.getId()), toRegion(orderEntity.getRegion().getId()),
                toCountry(orderEntity.getCountry().getId()), toItemType(orderEntity.getItemType().getId()),
                toSalesChannel(orderEntity.getSalesChannel()), toPriority(orderEntity.getPriority()),
                orderEntity.getDate(), orderEntity.getShipDate(), orderEntity.getUnitsSold(), orderEntity.getUnitPrice(), orderEntity.getUnitCost(),
                orderEntity.getTotalRevenue(), orderEntity.getTotalCost(), orderEntity.getTotalProfit());
    }

    private ItemType toItemType(Integer id) {
        return jpaItemTypeRepository.findById(id);
    }

    private static Priority toPriority(String priority) {
        return switch (priority) {
            case "L" -> Priority.LOW;
            case "M" -> Priority.MEDIUM;
            case "H" -> Priority.HIGH;
            case "C" -> Priority.CRITICAL;
            default -> null;
        };
    }

    private Region toRegion(Integer id) {
        return jpaRegionRepository.findyId(id);
    }

    private static SalesChannel toSalesChannel(String salesChannel) {
        return switch (salesChannel) {
            case "N" -> SalesChannel.ONLINE;
            case "F" -> SalesChannel.OFFLINE;
            default -> null;
        };
    }

    @Override
    public void truncate() {
        orderEntityRepository.deleteAllInBatch();
    }

    @Override
    @Transactional
    public void saveAll(List<Order> orders) {
        orderEntityRepository.saveAll(orders.stream().map(JpaOrderRepository::toEntity).collect(Collectors.toSet()));
    }

    private static CountryEntity toCountryEntity(Country country) {
        return new CountryEntity(country.getId().getValue(), country.getName());
    }

    private static OrderEntity toEntity(Order order) {
        return new OrderEntity(order.getId().getValue(), order.getUuid(), toRegionEntity(order.getRegion()), toCountryEntity(order.getCountry()),
                toItemTypeEntity(order.getItemType()), toSalesChannelId(order.getSalesChannel()), toPriorityId(order.getPriority()), order.getDate(),
                order.getShipDate(), order.getUnitsSold(), order.getUnitPrice(), order.getUnitCost(), order.getTotalRevenue(), order.getTotalCost(),
                order.getTotalProfit());
    }

    private static ItemTypeEntity toItemTypeEntity(ItemType itemType) {
        return new ItemTypeEntity(itemType.getId().getValue(), itemType.getName());
    }

    private static String toPriorityId(Priority priority) {
        return switch (priority) {
            case LOW -> "L";
            case MEDIUM -> "M";
            case HIGH -> "H";
            case CRITICAL -> "C";
        };
    }

    private static RegionEntity toRegionEntity(Region region) {
        return new RegionEntity(region.getId().getValue(), region.getName());
    }

    private static String toSalesChannelId(SalesChannel salesChannel) {
        return switch (salesChannel) {
            case ONLINE -> "N";
            case OFFLINE -> "F";
        };
    }
}
