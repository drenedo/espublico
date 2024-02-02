package me.renedo.espublico.orders.domain;

import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class RegionMother {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static Region any() {
        return new Region(new RegionId(Math.abs(RANDOM.nextInt())), RandomStringUtils.randomAlphanumeric(10));
    }

    public static Region any(String name) {
        return new Region(new RegionId(Math.abs(RANDOM.nextInt())), name);
    }
}
