package me.renedo.espublico.orders.domain;

public interface RegionRepository {

    Region findByNameOrCreate(String region);
}
