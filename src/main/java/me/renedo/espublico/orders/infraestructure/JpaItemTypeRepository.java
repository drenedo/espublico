package me.renedo.espublico.orders.infraestructure;

import org.springframework.stereotype.Component;

import me.renedo.espublico.orders.domain.ItemType;
import me.renedo.espublico.orders.domain.ItemTypeId;
import me.renedo.espublico.orders.domain.ItemTypeRepository;
import me.renedo.espublico.orders.infraestructure.jpa.ItemTypeEntity;
import me.renedo.espublico.orders.infraestructure.jpa.ItemTypeEntityRepository;

@Component
public class JpaItemTypeRepository implements ItemTypeRepository {

    private final ItemTypeEntityRepository itemTypeRepository;

    public JpaItemTypeRepository(ItemTypeEntityRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    @Override
    public ItemType findByNameOrCreate(String name) {
        return itemTypeRepository.findByName(name)
                .map(JpaItemTypeRepository::toDomain)
                .orElseGet(() -> create(name));
    }

    private ItemType create(String name) {
        ItemTypeEntity itemTypeEntity = new ItemTypeEntity(name);
        ItemTypeEntity saved = itemTypeRepository.save(itemTypeEntity);
        return toDomain(saved);
    }

    private static ItemType toDomain(ItemTypeEntity itemTypeEntity) {
        return new ItemType(new ItemTypeId(itemTypeEntity.getId()), itemTypeEntity.getName());
    }
}
