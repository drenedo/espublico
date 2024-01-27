package me.renedo.espublico.orders.infraestructure.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "region")
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_generator")
    @SequenceGenerator(name = "region_generator", sequenceName = "region_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    public RegionEntity() {

    }

    public RegionEntity(String name) {
        this.name = name;
    }

    public RegionEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
