package me.renedo.espublico.orders.domain;

public class Region {
    private final RegionId id;
    private final String name;

    public Region(RegionId id, String name) {
        this.id = id;
        this.name = name;
    }

    public RegionId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
