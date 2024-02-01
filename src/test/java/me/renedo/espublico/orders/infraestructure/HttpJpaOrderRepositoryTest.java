package me.renedo.espublico.orders.infraestructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import me.renedo.espublico.orders.domain.Country;
import me.renedo.espublico.orders.domain.CountryMother;
import me.renedo.espublico.orders.domain.ItemType;
import me.renedo.espublico.orders.domain.ItemTypeMother;
import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.PageOfOrders;
import me.renedo.espublico.orders.domain.Region;
import me.renedo.espublico.orders.domain.RegionMother;
import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository;
import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository.OrderDTO;
import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository.PageDTO;
import me.renedo.espublico.orders.infraestructure.rest.OrderDTOMother;
import me.renedo.espublico.orders.infraestructure.rest.PageDTOMother;

class HttpJpaOrderRepositoryTest {

    private final HttpOrderRepository httpOrderRepository = Mockito.mock(HttpOrderRepository.class);
    private final JpaCountryRepository countryRepository = Mockito.mock(JpaCountryRepository.class);
    private final JpaItemTypeRepository itemTypeRepository = Mockito.mock(JpaItemTypeRepository.class);
    private final JpaRegionRepository regionRepository = Mockito.mock(JpaRegionRepository.class);
    private final HttpPageOfOrderRepository repository =
            new HttpPageOfOrderRepository(httpOrderRepository, regionRepository, countryRepository, itemTypeRepository);

    @Test
    void test_first_page_when_data_is_correct() {
        // Given
        PageDTO page = PageDTOMother.any();
        OrderDTO orderDTO = page.content().stream().findFirst().get();
        when(httpOrderRepository.getPage(1, 100)).thenReturn(PageDTOMother.any());
        Country country = CountryMother.any(orderDTO.country());
        when(countryRepository.findByNameOrCreate(orderDTO.country())).thenReturn(country);
        ItemType itemType = ItemTypeMother.any(orderDTO.itemType());
        when(itemTypeRepository.findByNameOrCreate(orderDTO.itemType())).thenReturn(itemType);
        Region region = RegionMother.any(orderDTO.region());
        when(regionRepository.findByNameOrCreate(orderDTO.region())).thenReturn(region);

        // When
        PageOfOrders firstPage = repository.getFirstPage(100);

        // Then
        assertThat(firstPage).isNotNull();
        assertThat(firstPage.getOrders()).hasSize(1);
        Order order = firstPage.getOrders().stream().findFirst().get();
        assertThat(order.getCountry()).usingRecursiveComparison().isEqualTo(country);
        assertThat(order.getItemType()).usingRecursiveComparison().isEqualTo(itemType);
        assertThat(order.getRegion()).usingRecursiveComparison().isEqualTo(region);
        assertThat(order.getPriority().toString().substring(0, 1)).isEqualTo(orderDTO.priority());
    }

    @Test
    void test_url_when_data_is_correct() {
        // Given
        PageDTO page = PageDTOMother.any();
        OrderDTO orderDTO = page.content().stream().findFirst().get();
        when(httpOrderRepository.getPage("some-url")).thenReturn(PageDTOMother.any());
        Country country = CountryMother.any(orderDTO.country());
        when(countryRepository.findByNameOrCreate(orderDTO.country())).thenReturn(country);
        ItemType itemType = ItemTypeMother.any(orderDTO.itemType());
        when(itemTypeRepository.findByNameOrCreate(orderDTO.itemType())).thenReturn(itemType);
        Region region = RegionMother.any(orderDTO.region());
        when(regionRepository.findByNameOrCreate(orderDTO.region())).thenReturn(region);

        // When
        PageOfOrders firstPage = repository.getPage("some-url");

        // Then
        assertThat(firstPage).isNotNull();
        assertThat(firstPage.getOrders()).hasSize(1);
        Order order = firstPage.getOrders().stream().findFirst().get();
        assertThat(order.getCountry()).usingRecursiveComparison().isEqualTo(country);
        assertThat(order.getItemType()).usingRecursiveComparison().isEqualTo(itemType);
        assertThat(order.getRegion()).usingRecursiveComparison().isEqualTo(region);
        assertThat(order.getPriority().toString().substring(0, 1)).isEqualTo(orderDTO.priority());
    }

    @Test
    void test_first_page_when_priority_is_wrong() {
        // Given
        OrderDTO orderDTO = OrderDTOMother.anyWithPriority("bad-priority");
        PageDTO page = PageDTOMother.any(orderDTO);
        when(httpOrderRepository.getPage("some-url")).thenReturn(page);

        // When
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> repository.getPage("some-url"));

        // Then
        assertThat(thrown.getMessage()).isEqualTo("Priority not supported");
    }

    @Test
    void test_url_when_priority_is_wrong() {
        // Given
        OrderDTO orderDTO = OrderDTOMother.anyWithPriority("bad-priority");
        PageDTO page = PageDTOMother.any(orderDTO);
        when(httpOrderRepository.getPage(1, 100)).thenReturn(page);

        // When
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> repository.getFirstPage(100));

        // Then
        assertThat(thrown.getMessage()).isEqualTo("Priority not supported");
    }

    @Test
    void test_first_page_when_sales_chanel_is_wrong() {
        // Given
        OrderDTO orderDTO = OrderDTOMother.anyWithSalesChanel("bad-sales-chanel");
        PageDTO page = PageDTOMother.any(orderDTO);
        when(httpOrderRepository.getPage(1, 100)).thenReturn(page);

        // When
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> repository.getFirstPage(100));

        // Then
        assertThat(thrown.getMessage()).isEqualTo("Sales channel not supported");
    }

    @Test
    void test_url_when_sales_chanel_is_wrong() {
        // Given
        OrderDTO orderDTO = OrderDTOMother.anyWithSalesChanel("bad-sales-chanel");
        PageDTO page = PageDTOMother.any(orderDTO);
        when(httpOrderRepository.getPage("some-url")).thenReturn(page);

        // When
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> repository.getPage("some-url"));

        // Then
        assertThat(thrown.getMessage()).isEqualTo("Sales channel not supported");
    }
}
