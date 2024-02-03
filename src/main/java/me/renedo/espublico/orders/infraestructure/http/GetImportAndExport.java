package me.renedo.espublico.orders.infraestructure.http;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientException;

import me.renedo.espublico.orders.application.ExportInformation;
import me.renedo.espublico.orders.application.ExportUseCase;
import me.renedo.espublico.orders.application.ImportSummary;
import me.renedo.espublico.orders.application.ImportUseCase;

@RestController
public class GetImportAndExport {

    private final ImportUseCase importUseCase;

    private final ExportUseCase exportUseCase;

    public GetImportAndExport(ImportUseCase importUseCase, ExportUseCase exportUseCase) {
        this.importUseCase = importUseCase;
        this.exportUseCase = exportUseCase;
    }


    @GetMapping(value = "/v1/import-export")
    public ImportAndExport importExportV1() throws IOException {
        ImportSummary summary = importUseCase.execute();
        ExportInformation exportInformation = exportUseCase.execute();
        return new ImportAndExport(summary, exportInformation);
    }

    @ExceptionHandler({RuntimeException.class, WebClientException.class, IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    private Error handleException(Exception e) {
        return new Error(e.getMessage());
    }

    public record ImportAndExport(ImportSummary importSummary, ExportInformation exportInformation) {
    }
}
