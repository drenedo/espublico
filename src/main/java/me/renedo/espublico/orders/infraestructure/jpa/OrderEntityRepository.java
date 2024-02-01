package me.renedo.espublico.orders.infraestructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer>, PagingAndSortingRepository<OrderEntity, Integer> {

}
