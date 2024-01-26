package me.renedo.espublico.orders.domain;

public class OrderId {
    private final Long value;

    public OrderId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
