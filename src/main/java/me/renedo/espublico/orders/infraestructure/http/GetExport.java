package me.renedo.espublico.orders.infraestructure.http;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public ResponseEntity<ExportInformation> importV1() throws IOException {
        return ResponseEntity.ok(exportUseCase.execute());
    }

    @ExceptionHandler({RuntimeException.class, IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    private Error handleException(Exception e) {
        return new Error(e.getMessage());
    }
}
