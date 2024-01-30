package me.renedo.espublico.orders.infraestructure.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "sales_order")
public class OrderEntity implements Persistable<Long> {

    @Id
    private Long id;

    private UUID uuid;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private RegionEntity region;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private CountryEntity country;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
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

    @Transient
    private boolean isNew = true;

    public OrderEntity() {
    }

    public OrderEntity(Long id, UUID uuid, RegionEntity region, CountryEntity country, ItemTypeEntity itemType, String salesChannel, String priority,
            LocalDate date, LocalDate shipDate, Integer unitsSold, BigDecimal unitPrice, BigDecimal unitCost, BigDecimal totalRevenue,
            BigDecimal totalCost, BigDecimal totalProfit) {
        this.id = id;
        this.uuid = uuid;
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

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}
