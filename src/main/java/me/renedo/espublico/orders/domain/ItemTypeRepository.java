package me.renedo.espublico.orders.domain;

public interface ItemTypeRepository {

    ItemType findByNameOrCreate(String name);
}
