package me.renedo.espublico.orders.infraestructure.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_type")
public class ItemTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_type_generator")
    @SequenceGenerator(name = "item_type_generator", sequenceName = "item_type_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    public ItemTypeEntity() {
    }

    public ItemTypeEntity(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
