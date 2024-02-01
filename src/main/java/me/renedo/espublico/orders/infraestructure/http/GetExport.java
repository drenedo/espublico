package me.renedo.espublico.orders.infraestructure.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.espublico.orders.application.ExportInformation;
import me.renedo.espublico.orders.application.ExportUseCase;

@RestController
public class GetExport {

    private final ExportUseCase exportUseCase;

    public GetExport(ExportUseCase exportUseCase) {
        this.exportUseCase = exportUseCase;
    }

    @GetMapping(value = "/v1/export")
    public ExportInformation importV1() {
        return exportUseCase.execute();
    }
}
