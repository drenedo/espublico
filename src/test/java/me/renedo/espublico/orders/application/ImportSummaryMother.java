package me.renedo.espublico.orders.application;

import java.util.List;
import java.util.Map;

class ImportSummaryMother {

    public static ImportSummary anyOfErrors(List<ImportSummary.Error> errors) {
        return new ImportSummary(Map.of(), errors);
    }

    public static ImportSummary anyOfMap(String field1, Map<String, Integer> values1, String field2, Map<String, Integer> values2) {
        return new ImportSummary(Map.of(field1, values1, field2, values2), null);
    }
}
