package me.renedo.espublico.orders.infraestructure.jpa;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RegionEntityRepository extends CrudRepository<RegionEntity, Integer> {

    Optional<RegionEntity> findByName(String name);
}
