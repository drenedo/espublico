package me.renedo.espublico.orders.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Order {
    private final UUID uuid;
    private final OrderId id;
    private final Region region;
    private final Country country;
    private final ItemType itemType;
    private final SalesChannel salesChannel;
    private final Priority priority;
    private final LocalDate date;
    private final LocalDate shipDate;
    private final Integer unitsSold;
    private final BigDecimal unitPrice;
    private final BigDecimal unitCost;
    private final BigDecimal totalRevenue;
    private final BigDecimal totalCost;
    private final BigDecimal totalProfit;

    public Order(UUID uuid, OrderId id, Region region, Country country, ItemType itemType, SalesChannel salesChannel, Priority priority,
            LocalDate date, LocalDate shipDate, Integer unitsSold, BigDecimal unitPrice, BigDecimal unitCost, BigDecimal totalRevenue,
            BigDecimal totalCost, BigDecimal totalProfit) {
        this.uuid = uuid;
        this.id = id;
        this.region = region;
        this.country = country;
        this.itemType = itemType;
        this.salesChannel = salesChannel;
        this.priority = priority;
        this.date = date;
        this.shipDate = shipDate;
        this.unitsSold = unitsSold;
        this.unitPrice = unitPrice;
        this.unitCost = unitCost;
        this.totalRevenue = totalRevenue;
        this.totalCost = totalCost;
        this.totalProfit = totalProfit;
    }

    public Country getCountry() {
        return country;
    }

    public LocalDate getDate() {
        return date;
    }

    public OrderId getId() {
        return id;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Priority getPriority() {
        return priority;
    }

    public Region getRegion() {
        return region;
    }

    public SalesChannel getSalesChannel() {
        return salesChannel;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public UUID getUuid() {
        return uuid;
    }
}
