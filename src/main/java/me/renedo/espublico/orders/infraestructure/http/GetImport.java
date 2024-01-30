package me.renedo.espublico.orders.infraestructure.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.espublico.orders.application.ImportSummary;
import me.renedo.espublico.orders.application.ImportUseCase;

@RestController
public class GetImport {

    private final ImportUseCase importUseCase;

    public GetImport(ImportUseCase importUseCase) {
        this.importUseCase = importUseCase;
    }

    @GetMapping(value = "/v1/import")
    public ImportSummary importV1() {
        return importUseCase.execute();
    }

}
