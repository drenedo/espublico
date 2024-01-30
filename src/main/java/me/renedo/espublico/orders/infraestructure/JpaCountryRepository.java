package me.renedo.espublico.orders.infraestructure;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import me.renedo.espublico.orders.domain.Country;
import me.renedo.espublico.orders.domain.CountryId;
import me.renedo.espublico.orders.domain.CountryRepository;
import me.renedo.espublico.orders.infraestructure.jpa.CountryEntity;
import me.renedo.espublico.orders.infraestructure.jpa.CountryEntityRepository;

@Component
@CacheConfig(cacheNames = "countries")
public class JpaCountryRepository implements CountryRepository {

    private final CountryEntityRepository countryEntityRepository;

    public JpaCountryRepository(CountryEntityRepository countryEntityRepository) {
        this.countryEntityRepository = countryEntityRepository;
    }

    @Override
    @Cacheable
    public Country findByNameOrCreate(String name) {
        return countryEntityRepository.findByName(name)
                .map(JpaCountryRepository::toDomain)
                .orElseGet(() -> create(name));
    }

    private Country create(String name) {
        CountryEntity countryEntity = new CountryEntity(name);
        CountryEntity saved = countryEntityRepository.save(countryEntity);
        return toDomain(saved);
    }

    private static Country toDomain(CountryEntity countryEntity) {
        return new Country(new CountryId(countryEntity.getId()), countryEntity.getName());
    }
}
