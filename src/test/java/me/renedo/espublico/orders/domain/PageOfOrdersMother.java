package me.renedo.espublico.orders.domain;

import java.security.SecureRandom;
import java.util.stream.IntStream;

public class PageOfOrdersMother {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static PageOfOrders anyWithNextUrl(int elements) {
        return new PageOfOrders(RANDOM.nextInt(), IntStream.range(0, elements).mapToObj(i -> OrderMother.any()).toList(),
                "some-url");
    }

    public static PageOfOrders anyWithNoNextUrl(int elements) {
        return new PageOfOrders(RANDOM.nextInt(), IntStream.range(0, elements).mapToObj(i -> OrderMother.any()).toList(), null);
    }

}
