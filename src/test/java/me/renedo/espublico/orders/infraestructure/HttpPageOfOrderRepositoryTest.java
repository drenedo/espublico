package me.renedo.espublico.orders.infraestructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import me.renedo.espublico.orders.domain.PageOfOrders;
import me.renedo.espublico.orders.infraestructure.rest.HttpOrderRepository;
import me.renedo.espublico.orders.infraestructure.rest.PageDTOMother;

class HttpPageOfOrderRepositoryTest {
    private final HttpOrderRepository httpOrderRepository = Mockito.mock(HttpOrderRepository.class);

    private final JpaRegionRepository regionRepository = Mockito.mock(JpaRegionRepository.class);

    private final JpaCountryRepository countryRepository = Mockito.mock(JpaCountryRepository.class);

    private final JpaItemTypeRepository itemTypeRepository = Mockito.mock(JpaItemTypeRepository.class);

    private final HttpPageOfOrderRepository repository =
            new HttpPageOfOrderRepository(httpOrderRepository, regionRepository, countryRepository, itemTypeRepository);

    private static void assertDataMapIsCorrect(PageOfOrders page, HttpOrderRepository.PageDTO pageOrder) {
        assertThat(page).isNotNull();
        assertThat(page.getOrders()).hasSize(1);
        HttpOrderRepository.OrderDTO orderDTO = pageOrder.content().stream().findFirst().get();
        assertThat(page.getOrders().get(0).getId().getValue()).isEqualTo(orderDTO.id());
        assertThat(page.getOrders().get(0).getUuid()).hasToString(orderDTO.uuid());
        assertThat(page.getOrders().get(0).getUnitCost().doubleValue()).isEqualTo(orderDTO.unitCost());
        assertThat(page.getOrders().get(0).getTotalCost().doubleValue()).isEqualTo(orderDTO.totalCost());
        assertThat(page.getOrders().get(0).getTotalProfit().doubleValue()).isEqualTo(orderDTO.totalProfit());
        assertThat(page.getOrders().get(0).getTotalRevenue().doubleValue()).isEqualTo(orderDTO.totalRevenue());
        assertThat(page.getOrders().get(0).getUnitPrice().doubleValue()).isEqualTo(orderDTO.unitPrice());
    }

    @Test
    void get_page_on_sales_chanel_error() {
        // Given
        Mockito.doReturn(PageDTOMother.anyWithSalesChanel("Wrong channel")).when(httpOrderRepository).getPage("some-url");

        // When
        PageOfOrders page = repository.getPage("some-url");

        // Then
        assertThat(page).isNotNull();
        assertThat(page.getOrders()).hasSize(1);
        assertThat(page.getOrders().get(0).getSalesChannel()).isNull();
    }

    @Test
    void get_page_on_priority_error() {
        // Given
        Mockito.doReturn(PageDTOMother.anyWithPriority("Wrong priority")).when(httpOrderRepository).getPage("some-url");

        // When
        PageOfOrders page = repository.getPage("some-url");

        // Then
        assertThat(page).isNotNull();
        assertThat(page.getOrders()).hasSize(1);
        assertThat(page.getOrders().get(0).getPriority()).isNull();
    }

    @Test
    void get_correct_page_by_url() {
        // Given
        HttpOrderRepository.PageDTO pageOrder = PageDTOMother.any();
        Mockito.doReturn(pageOrder).when(httpOrderRepository).getPage("some-url");

        // When
        PageOfOrders page = repository.getPage("some-url");

        // Then
        assertDataMapIsCorrect(page, pageOrder);
    }

    @Test
    void get_correct_page_by_number() {
        // Given
        HttpOrderRepository.PageDTO pageOrder = PageDTOMother.any();
        Mockito.doReturn(pageOrder).when(httpOrderRepository).getPage(1, 10);

        // When
        PageOfOrders page = repository.getFirstPage(10);

        // Then
        assertDataMapIsCorrect(page, pageOrder);
    }

}
