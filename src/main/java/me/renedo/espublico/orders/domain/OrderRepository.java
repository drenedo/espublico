package me.renedo.espublico.orders.domain;

import java.util.List;

public interface OrderRepository {

    void saveAll(List<Order> orders);
}
