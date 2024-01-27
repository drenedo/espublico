package me.renedo.espublico.orders.infraestructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import me.renedo.espublico.TestEspublicoApplication;
import me.renedo.espublico.orders.domain.ItemTypeMother;

@SpringBootTest
@Import(TestEspublicoApplication.class)
@Transactional
class JpaRegionRepositoryTest {

    @Autowired
    private JpaRegionRepository jpaRegionRepository;

    @Test
    @Sql("/create_entities.sql")
    void ensure_find_by_name_returns_the_country_when_exists() {
        assertThat(jpaRegionRepository.findByNameOrCreate("Europe"))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(ItemTypeMother.any("Europe"));
    }

    @Test
    @DirtiesContext
    void ensure_create_returns_the_country_when_exists() {
        assertThat(jpaRegionRepository.findByNameOrCreate("Midle East"))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(ItemTypeMother.any("Midle East"));
    }
}
