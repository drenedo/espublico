package me.renedo.espublico.orders.domain;

public interface PageOfOrdersRepository {

    PageOfOrders getFirstPage(int size);

    PageOfOrders getPage(String url);
}
