package me.renedo.espublico.orders.domain;

import java.util.List;

public class PageOfOrders {

    private final int page;

    private final List<Order> orders;

    private final String nextUrl;

    public PageOfOrders(int page, List<Order> orders, String nextUrl) {
        this.page = page;
        this.orders = orders;
        this.nextUrl = nextUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getPage() {
        return page;
    }
}
