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
import me.renedo.espublico.orders.domain.CountryMother;


@SpringBootTest
@Import(TestEspublicoApplication.class)
@Transactional
class JpaCountryRepositoryTest {

    @Autowired
    private JpaCountryRepository jpaCountryRepository;

    @Test
    @Sql("/create_entities.sql")
    void ensure_find_by_name_returns_the_country_when_exists() {
        assertThat(jpaCountryRepository.findByNameOrCreate("United States"))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(CountryMother.any("United States"));
    }

    @Test
    @DirtiesContext
    void ensure_create_returns_the_country_when_exists() {
        assertThat(jpaCountryRepository.findByNameOrCreate("Spain"))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(CountryMother.any("Spain"));
    }
}
