package me.renedo.espublico.orders.domain;

import java.util.Set;

public class PageOfOrders {

    private final Set<Order> orders;

    private final String nextUrl;

    public PageOfOrders(Set<Order> orders, String nextUrl) {
        this.orders = orders;
        this.nextUrl = nextUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public Set<Order> getOrders() {
        return orders;
    }
}
