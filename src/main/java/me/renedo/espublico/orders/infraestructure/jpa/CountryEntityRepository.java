package me.renedo.espublico.orders.infraestructure.jpa;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CountryEntityRepository extends CrudRepository<CountryEntity, Integer> {

    Optional<CountryEntity> findByName(String name);
}
