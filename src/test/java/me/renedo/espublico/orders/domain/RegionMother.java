package me.renedo.espublico.orders.domain;

import java.security.SecureRandom;

public class RegionMother {

    private final static SecureRandom RANDOM = new SecureRandom();

    public static Region any(String name) {
        return new Region(new RegionId(RANDOM.nextInt()), name);
    }
}
