package me.renedo.espublico.orders.infraestructure.rest;

import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository.LinksDTO;

public class LinksDTOMother {

    public static LinksDTO any() {
        return new LinksDTO("any-self", "any-next", "any-previous");
    }
}
