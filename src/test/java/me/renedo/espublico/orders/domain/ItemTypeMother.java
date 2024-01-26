package me.renedo.espublico.orders.domain;

import java.security.SecureRandom;

public class ItemTypeMother {

    private final static SecureRandom RANDOM = new SecureRandom();

    public static ItemType any(String name) {
        return new ItemType(new ItemTypeId(RANDOM.nextInt()), name);
    }

    public static ItemType any(Integer id, String name) {
        return new ItemType(new ItemTypeId(id), name);
    }
}
