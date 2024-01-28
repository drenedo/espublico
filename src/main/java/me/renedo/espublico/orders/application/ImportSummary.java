package me.renedo.espublico.orders.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportSummary {
    private final Map<String, Map<String, Integer>> summary;

    private final List<Error> errors;

    public ImportSummary(Map<String, Map<String, Integer>> summary, List<Error> errors) {
        this.summary = summary;
        this.errors = errors;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public Map<String, Map<String, Integer>> getSummary() {
        return summary;
    }

    public ImportSummary merge(ImportSummary other) {
        HashMap<String, Map<String, Integer>> mergedSummary = new HashMap<>(summary);
        other.getSummary().forEach((field, values) -> {
            Map<String, Integer> mergedValues = new HashMap<>(mergedSummary.getOrDefault(field, new HashMap<>()));
            values.forEach((value, count) -> {
                mergedValues.put(value, mergedValues.getOrDefault(value, 0) + count);
            });
            mergedSummary.put(field, mergedValues);
        });
        return new ImportSummary(mergedSummary, Stream.concat(toStream(errors), toStream(other.getErrors())).collect(Collectors.toList()));
    }

    private static Stream<Error> toStream(List<Error> errors) {
        return errors != null ? errors.stream() : Stream.empty();
    }

    public record Error(int page, String message) {
    }
}
