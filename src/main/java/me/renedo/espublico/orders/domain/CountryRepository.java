package me.renedo.espublico.orders.domain;

public interface CountryRepository {

    Country findByNameOrCreate(String name);
}
