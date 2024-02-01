package me.renedo.espublico.orders.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import me.renedo.espublico.orders.domain.Order;
import me.renedo.espublico.orders.domain.OrderRepository;
import me.renedo.espublico.orders.domain.PageOfOrders;
import me.renedo.espublico.orders.domain.PageOfOrdersMother;
import me.renedo.espublico.orders.domain.PageOfOrdersRepository;

class ImportUseCaseTest {
    private final PageOfOrdersRepository pageOfOrdersRepository = Mockito.mock(PageOfOrdersRepository.class);
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @Test
    void should_call_repositories_only_once() {
        // Given
        PageOfOrders pageOfOrders = PageOfOrdersMother.anyWithNoNextUrl(5);
        ImportUseCase useCase = givenImportUseCase(10, 10);
        doReturn(pageOfOrders).when(pageOfOrdersRepository).getFirstPage(10);

        // When
        ImportSummary summary = useCase.execute();

        // Then
        verify(pageOfOrdersRepository, Mockito.times(1)).getFirstPage(10);
        verify(orderRepository, Mockito.times(1)).saveAll(pageOfOrders.getOrders());
        assertSummaySize(summary, 5);

    }

    @Test
    void should_call_order_repository_ten_times() {
        // Given
        PageOfOrders pageOfOrders = PageOfOrdersMother.anyWithNoNextUrl(100);
        ImportUseCase useCase = givenImportUseCase(100, 10);
        doReturn(pageOfOrders).when(pageOfOrdersRepository).getFirstPage(100);

        // When
        ImportSummary summary = useCase.execute();

        // Then
        verify(pageOfOrdersRepository, Mockito.times(1)).getFirstPage(100);
        ArgumentCaptor<List<Order>> captor = ArgumentCaptor.forClass(List.class);
        verify(orderRepository, Mockito.times(10)).saveAll(captor.capture());
        assertSizesOfOrdersCalls(captor, List.of(10));
        assertSummaySize(summary, 100);
    }

    @Test
    void should_call_order_repository_two_times() {
        // Given
        PageOfOrders pageOfOrders = PageOfOrdersMother.anyWithNoNextUrl(80);
        ImportUseCase useCase = givenImportUseCase(100, 50);
        doReturn(pageOfOrders).when(pageOfOrdersRepository).getFirstPage(100);

        // When
        ImportSummary summary = useCase.execute();

        // Then
        verify(pageOfOrdersRepository, Mockito.times(1)).getFirstPage(100);
        ArgumentCaptor<List<Order>> captor = ArgumentCaptor.forClass(List.class);
        verify(orderRepository, Mockito.times(2)).saveAll(captor.capture());
        assertSizesOfOrdersCalls(captor, List.of(50, 30));
        assertSummaySize(summary, 80);
    }

    @Test
    void should_call_page_orders_repository_two_times() {
        // Given
        PageOfOrders firstPageOfOrders = PageOfOrdersMother.anyWithNextUrl(80);
        PageOfOrders secondPageOfOrders = PageOfOrdersMother.anyWithNoNextUrl(42);
        ImportUseCase useCase = givenImportUseCase(80, 50);
        doReturn(firstPageOfOrders).when(pageOfOrdersRepository).getFirstPage(80);
        doReturn(secondPageOfOrders).when(pageOfOrdersRepository).getPage(firstPageOfOrders.getNextUrl());

        // When
        ImportSummary summary = useCase.execute();

        // Then
        verify(pageOfOrdersRepository, Mockito.times(1)).getFirstPage(80);
        verify(pageOfOrdersRepository, Mockito.times(1)).getPage(firstPageOfOrders.getNextUrl());
        ArgumentCaptor<List<Order>> captor = ArgumentCaptor.forClass(List.class);
        verify(orderRepository, Mockito.times(3)).saveAll(captor.capture());
        assertSizesOfOrdersCalls(captor, List.of(50, 30, 42));
        assertSummaySize(summary, 80 + 42);
    }

    private static void assertSummaySize(ImportSummary summary, int expectedSize) {
        assertThat(summary.getSummary().get("region").values().stream().mapToInt(Integer::intValue).sum()).isEqualTo(expectedSize);
        assertThat(summary.getSummary().get("country").values().stream().mapToInt(Integer::intValue).sum()).isEqualTo(expectedSize);
        assertThat(summary.getSummary().get("itemType").values().stream().mapToInt(Integer::intValue).sum()).isEqualTo(expectedSize);
        assertThat(summary.getSummary().get("priority").values().stream().mapToInt(Integer::intValue).sum()).isEqualTo(expectedSize);
        assertThat(summary.getSummary().get("salesChannel").values().stream().mapToInt(Integer::intValue).sum()).isEqualTo(expectedSize);
    }

    private static void assertSizesOfOrdersCalls(ArgumentCaptor<List<Order>> captor, List<Integer> values) {
        Set<Integer> sizes = captor.getAllValues().stream().map(List::size).collect(Collectors.toSet());
        values.forEach(value -> assertThat(sizes).contains(value));
    }

    private ImportUseCase givenImportUseCase(int httpPageSize, int jpaPageSize) {
        return new ImportUseCase(pageOfOrdersRepository, orderRepository, httpPageSize, jpaPageSize);
    }
}
