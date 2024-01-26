package me.renedo.espublico.orders.infraestructure.jpa;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ItemTypeEntityRepository extends CrudRepository<ItemTypeEntity, Integer> {

    Optional<ItemTypeEntity> findByName(String name);
}
