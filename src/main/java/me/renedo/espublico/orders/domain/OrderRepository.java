package me.renedo.espublico.orders.domain;

import java.util.Set;

public interface OrderRepository {

    void saveAll(Set<Order> orders);
}
