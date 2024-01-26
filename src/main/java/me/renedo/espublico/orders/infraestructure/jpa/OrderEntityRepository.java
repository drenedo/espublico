package me.renedo.espublico.orders.infraestructure.jpa;

import org.springframework.data.repository.CrudRepository;

public interface OrderEntityRepository extends CrudRepository<OrderEntity, Integer> {
}
