package me.renedo.espublico.orders.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ImportSummaryTest {


    @Test
    public void should_join_errors() {
        // Given
        ImportSummary.Error error1 = ErrorMother.any();
        ImportSummary.Error error2 = ErrorMother.any();
        ImportSummary.Error error3 = ErrorMother.any();
        ImportSummary summary = ImportSummaryMother.anyOfErrors(List.of(error1, error2));

        // When
        ImportSummary merged = summary.merge(ImportSummaryMother.anyOfErrors(List.of(error3)));

        // Then
        assertThat(merged.getErrors()).containsExactlyInAnyOrder(error1, error2, error3);
    }

    @Test
    public void should_merge_values() {
        // Given
        ImportSummary summary = ImportSummaryMother.anyOfMap("country", Map.of("Spain", 10, "France", 23), "region",
                Map.of("EMEA", 42, "APAC", 12));

        // When
        ImportSummary merged = summary.merge(ImportSummaryMother.anyOfMap("country", Map.of("Italy", 4, "France", 3), "region",
                Map.of("EMEA", 12, "APAC", 22)));

        // Then
        assertThat(merged.getSummary().get("country")).containsEntry("Spain", 10).containsEntry("France", 26).containsEntry("Italy", 4);
        assertThat(merged.getSummary().get("region")).containsEntry("EMEA", 54).containsEntry("APAC", 34);
    }

    @Test
    public void should_merge_values_when_empty() {
        // Given
        ImportSummary summary = ImportSummaryMother.anyOfMap("country", Map.of(), "region", Map.of());

        // When
        ImportSummary merged = summary.merge(ImportSummaryMother.anyOfMap("country", Map.of("Italy", 4, "France", 3), "region",
                Map.of("EMEA", 12, "APAC", 22)));

        // Then
        assertThat(merged.getSummary().get("country")).containsEntry("Italy", 4).containsEntry("France", 3);
        assertThat(merged.getSummary().get("region")).containsEntry("EMEA", 12).containsEntry("APAC", 22);
    }
}
