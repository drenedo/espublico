package me.renedo.espublico.orders.infraestructure;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import me.renedo.espublico.TestEspublicoApplication;
import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderMother;
import me.renedo.espublico.orders.infraestructure.jpa.CountryEntityRepository;
import me.renedo.espublico.orders.infraestructure.jpa.ItemTypeEntityRepository;
import me.renedo.espublico.orders.infraestructure.jpa.OrderEntity;
import me.renedo.espublico.orders.infraestructure.jpa.OrderEntityRepository;
import me.renedo.espublico.orders.infraestructure.jpa.RegionEntityRepository;

@SpringBootTest
@Import(TestEspublicoApplication.class)
class JpaOrderRepositoryTest {

    @Autowired
    private JpaOrderRepository repository;

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @Autowired
    private CountryEntityRepository countryEntityRepository;

    @Autowired
    private ItemTypeEntityRepository itemTypeEntityRepository;

    @Autowired
    private RegionEntityRepository regionEntityRepository;

    @Test
    void ensure_persists_a_group_of_orders() {
        // Given
        List<Order> orders = IntStream.range(0, 40).mapToObj(i -> OrderMother.any()).collect(toList());

        // When
        repository.saveAll(orders);

        // Then
        Set<OrderEntity> entities = StreamSupport.stream(orderEntityRepository.findAll().spliterator(), false).collect(toSet());
        assertThat(entities).hasSize(40);
        entities.forEach(entity -> orders.stream().filter(order -> order.getId().getValue().equals(entity.getId()))
                .findFirst().ifPresentOrElse(order -> assertThat(entity).usingRecursiveComparison()
                        .comparingOnlyFields("uuid", "date", "shipDate", "unitsSold", "unitPrice", "unitCost",
                                "totalRevenue", "totalCost", "totalProfit")
                        .isEqualTo(order), () -> fail("Entity not mached with any order")));
    }

    @AfterEach
    void tear_down() {
        orderEntityRepository.deleteAll();
        countryEntityRepository.deleteAll();
        itemTypeEntityRepository.deleteAll();
        regionEntityRepository.deleteAll();
    }

}
