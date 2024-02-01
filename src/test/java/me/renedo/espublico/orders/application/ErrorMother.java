package me.renedo.espublico.orders.application;

import java.security.SecureRandom;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public class ErrorMother {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static ImportSummary.Error any() {
        return new ImportSummary.Error(RANDOM.nextInt(), RandomStringUtils.randomAlphanumeric(20));
    }
}
