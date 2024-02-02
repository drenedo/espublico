package me.renedo.espublico;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import me.renedo.espublico.orders.application.ExportInformation;
import me.renedo.espublico.orders.application.ExportUseCase;
import me.renedo.espublico.orders.domain.Country;
import me.renedo.espublico.orders.domain.ItemType;
import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderMother;
import me.renedo.espublico.orders.domain.Region;
import me.renedo.espublico.orders.infraestructure.JpaCountryRepository;
import me.renedo.espublico.orders.infraestructure.JpaItemTypeRepository;
import me.renedo.espublico.orders.infraestructure.JpaOrderRepository;
import me.renedo.espublico.orders.infraestructure.JpaRegionRepository;

@SpringBootTest
@Import(TestEspublicoApplication.class)
class EspublicoExportUseCaseIntegrationTests {

    @Autowired
    private ExportUseCase useCase;

    @Autowired
    private JpaOrderRepository repository;

    @Autowired
    private JpaRegionRepository regionRepository;

    @Autowired
    private JpaCountryRepository countryRepository;

    @Autowired
    private JpaItemTypeRepository itemTypeRepository;

    @Test
    void ensure_export_to_file_without_errors() throws IOException {
        // Given
        Country country = countryRepository.findByNameOrCreate("any-country");
        ItemType itemType = itemTypeRepository.findByNameOrCreate("any-item-type");
        Region region = regionRepository.findByNameOrCreate("any-region");
        List<Order> list = IntStream.range(0, 50).mapToObj(i -> OrderMother.any(country, region, itemType)).toList();
        repository.saveAll(list);

        // When
        ExportInformation information = useCase.execute();

        // Then
        try (Stream<String> lines = Files.lines(Paths.get(information.getPath()))) {
            assertThat(lines).hasSize(51);
        }
        Paths.get(information.getPath()).toFile().delete();
    }

    @AfterEach
    void clean_orders() {
        repository.truncate();
    }

}
