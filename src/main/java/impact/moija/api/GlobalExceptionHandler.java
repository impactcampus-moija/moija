package impact.moija.api;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public BaseResponse<Void> apiExceptionHandler(ApiException e) {
        return BaseResponse.fail(e);
    }
}
