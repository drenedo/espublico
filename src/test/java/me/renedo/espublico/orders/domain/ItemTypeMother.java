package me.renedo.espublico.orders.domain;

import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class ItemTypeMother {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static ItemType any() {
        return new ItemType(new ItemTypeId(RANDOM.nextInt()), RandomStringUtils.randomAlphanumeric(10));
    }

    public static ItemType any(String name) {
        return new ItemType(new ItemTypeId(RANDOM.nextInt()), name);
    }

    public static ItemType any(Integer id, String name) {
        return new ItemType(new ItemTypeId(id), name);
    }
}
