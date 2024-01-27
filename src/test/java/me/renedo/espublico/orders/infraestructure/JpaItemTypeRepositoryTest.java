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
class JpaItemTypeRepositoryTest {

    @Autowired
    private JpaItemTypeRepository jpaItemTypeRepository;

    @Test
    @Sql("/create_entities.sql")
    void ensure_find_by_name_returns_the_country_when_exists() {
        assertThat(jpaItemTypeRepository.findByNameOrCreate("Product"))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(ItemTypeMother.any("Product"));
    }

    @Test
    @DirtiesContext
    void ensure_create_returns_the_country_when_exists() {
        assertThat(jpaItemTypeRepository.findByNameOrCreate("Service"))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(ItemTypeMother.any("Service"));
    }
}
