package me.renedo.espublico;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestEspublicoApplication.class)
class EspublicoApplicationTests {

    @Test
    void ensure_application_starts_with_a_database_without_errors() {
    }

}
