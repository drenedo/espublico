package me.renedo.espublico.orders.infraestructure.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_order")
public class OrderEntity {

    @Id
    private Long id;

    private UUID uuid;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RegionEntity region;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CountryEntity country;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ItemTypeEntity itemType;

    @Column(name = "sales_channel")
    private String salesChannel;

    private String priority;

    private LocalDate date;

    @Column(name = "ship_date")
    private LocalDate shipDate;

    @Column(name = "units_sold")
    private Integer unitsSold;

    @Column(name = "unit_price", precision = 5)
    private BigDecimal unitPrice;

    @Column(name = "unit_cost", precision = 5)
    private BigDecimal unitCost;

    @Column(name = "total_revenue", precision = 5)
    private BigDecimal totalRevenue;

    @Column(name = "total_cost", precision = 5)
    private BigDecimal totalCost;

    @Column(name = "total_profit", precision = 5)
    private BigDecimal totalProfit;

    public CountryEntity getCountry() {
        return country;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public ItemTypeEntity getItemType() {
        return itemType;
    }

    public String getPriority() {
        return priority;
    }

    public RegionEntity getRegion() {
        return region;
    }

    public String getSalesChannel() {
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
