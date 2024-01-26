package me.renedo.espublico.orders.domain;

public class Country {
    private final CountryId id;
    private final String name;

    public Country(CountryId id, String name) {
        this.id = id;
        this.name = name;
    }

    public CountryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
