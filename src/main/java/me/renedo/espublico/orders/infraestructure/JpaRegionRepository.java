package me.renedo.espublico.orders.infraestructure;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import me.renedo.espublico.orders.domain.Region;
import me.renedo.espublico.orders.domain.RegionId;
import me.renedo.espublico.orders.domain.RegionRepository;
import me.renedo.espublico.orders.infraestructure.jpa.RegionEntity;
import me.renedo.espublico.orders.infraestructure.jpa.RegionEntityRepository;

@Component
@CacheConfig(cacheNames = "region")
public class JpaRegionRepository implements RegionRepository {

    private final RegionEntityRepository regionEntityRepository;

    public JpaRegionRepository(RegionEntityRepository regionEntityRepository) {
        this.regionEntityRepository = regionEntityRepository;
    }

    @Override
    @Cacheable
    public Region findByNameOrCreate(String name) {
        return regionEntityRepository.findByName(name)
                .map(JpaRegionRepository::toDomain)
                .orElseGet(() -> create(name));
    }

    private Region create(String name) {
        RegionEntity regionEntity = new RegionEntity(name);
        RegionEntity saved = regionEntityRepository.save(regionEntity);
        return toDomain(saved);
    }

    private static Region toDomain(RegionEntity regionEntity) {
        return new Region(new RegionId(regionEntity.getId()), regionEntity.getName());
    }
}
