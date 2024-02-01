package me.renedo.espublico.orders.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import me.renedo.espublico.orders.domain.OrderMother;
import me.renedo.espublico.orders.domain.OrderRepository;

class ExportUseCaseTest {
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    private ExportUseCase givenExportUseCase(int pageSize) {
        return new ExportUseCase(orderRepository, pageSize);
    }

    @Test
    void should_create_empty_file() throws IOException {
        // Given
        ExportUseCase useCase = givenExportUseCase(10);

        // When
        ExportInformation information = useCase.execute();

        // Then
        File file = new File(information.getPath());
        assertThat(Files.lines(file.toPath()).count()).isEqualTo(1);
    }

    @Test
    void should_call_repositories_three_times_and_file_has_expected_lines() throws IOException {
        // Given
        ExportUseCase useCase = givenExportUseCase(10);
        doReturn(IntStream.range(0, 10).mapToObj(i -> OrderMother.any()).toList())
                .when(orderRepository).getPage(0, 10);
        doReturn(IntStream.range(0, 10).mapToObj(i -> OrderMother.any()).toList())
                .when(orderRepository).getPage(1, 10);
        doReturn(IntStream.range(0, 3).mapToObj(i -> OrderMother.any()).toList())
                .when(orderRepository).getPage(2, 10);

        // When
        ExportInformation information = useCase.execute();

        // Then
        File file = new File(information.getPath());
        verify(orderRepository, times(3)).getPage(Mockito.anyInt(), Mockito.anyInt());
        assertThat(Files.lines(file.toPath()).count()).isEqualTo(24);
    }

    @Test
    void should_format_date() throws IOException {
        // Given
        ExportUseCase useCase = givenExportUseCase(10);
        doReturn(List.of(OrderMother.any())).when(orderRepository).getPage(0, 10);

        // When
        ExportInformation information = useCase.execute();

        // Then
        File file = new File(information.getPath());
        verify(orderRepository, times(1)).getPage(Mockito.anyInt(), Mockito.anyInt());
        try (Stream<String> lines = Files.lines(file.toPath())) {
            assertThat(lines.count()).isEqualTo(2);
        }
        try (Stream<String> lines = Files.lines(file.toPath())) {
            assertTrue(lines.toList().get(1).contains(LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy"))));
        }

    }
}
