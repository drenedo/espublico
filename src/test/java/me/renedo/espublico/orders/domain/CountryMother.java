package me.renedo.espublico.orders.domain;

import java.security.SecureRandom;

public class CountryMother {

    private final static SecureRandom RANDOM = new SecureRandom();

    public static Country any(String name) {
        return new Country(new CountryId(RANDOM.nextInt()), name);
    }

    public static Country any(Integer id, String name) {
        return new Country(new CountryId(id), name);
    }
}
