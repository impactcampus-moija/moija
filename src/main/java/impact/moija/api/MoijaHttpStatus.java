package impact.moija.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MoijaHttpStatus {
    /**
     * 200
     */
    OK(200, HttpStatus.OK, "요청이 정상적으로 수행되었습니다."),
    CREATED(201, HttpStatus.CREATED, "리소스를 생성하였습니다."),
    ;

    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String message;
}
