package me.renedo.espublico.orders.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;

public class OrderMother {

    private final static SecureRandom RANDOM = new SecureRandom();

    public static Order any() {
        return new Order(UUID.randomUUID(), new OrderId(RANDOM.nextLong()), RegionMother.any(), CountryMother.any(), ItemTypeMother.any(),
                SalesChannel.values()[RANDOM.nextInt(SalesChannel.values().length)], Priority.values()[RANDOM.nextInt(Priority.values().length)],
                LocalDate.now(), LocalDate.now(), RANDOM.nextInt(),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN));
    }

    public static Order any(Country country, Region region, ItemType itemType) {
        return new Order(UUID.randomUUID(), new OrderId(RANDOM.nextLong()), region, country, itemType,
                SalesChannel.values()[RANDOM.nextInt(SalesChannel.values().length)], Priority.values()[RANDOM.nextInt(Priority.values().length)],
                LocalDate.now(), LocalDate.now(), RANDOM.nextInt(),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(RANDOM.nextDouble()).setScale(2, RoundingMode.HALF_EVEN));
    }
}
