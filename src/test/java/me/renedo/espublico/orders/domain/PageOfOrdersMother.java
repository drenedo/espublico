package me.renedo.espublico.orders.domain;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageOfOrdersMother {

    public static PageOfOrders anyWithNextUrl(int elements) {
        return new PageOfOrders(IntStream.range(0, elements).mapToObj(i -> OrderMother.any()).collect(Collectors.toList()), "some-url");
    }

    public static PageOfOrders anyWithNoNextUrl(int elements) {
        return new PageOfOrders(IntStream.range(0, elements).mapToObj(i -> OrderMother.any()).collect(Collectors.toList()), null);
    }

}
