package me.renedo.espublico.orders.infraestructure.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientException;

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


    @ExceptionHandler({RuntimeException.class, WebClientException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    private Error handleException(Exception e) {
        return new Error(e.getMessage());
    }
}
