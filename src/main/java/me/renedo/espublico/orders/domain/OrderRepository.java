package me.renedo.espublico.orders.domain;

import java.util.List;

public interface OrderRepository {

    List<Order> getPage(int page, int size);

    void truncate();

    void saveAll(List<Order> orders);
}
