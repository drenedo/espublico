package me.renedo.espublico.orders.domain;

import java.util.List;

public class PageOfOrders {

    private final List<Order> orders;

    private final String nextUrl;

    public PageOfOrders(List<Order> orders, String nextUrl) {
        this.orders = orders;
        this.nextUrl = nextUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
