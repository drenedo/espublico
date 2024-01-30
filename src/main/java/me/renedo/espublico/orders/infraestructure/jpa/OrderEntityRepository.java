package me.renedo.espublico.orders.infraestructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer> {

}
