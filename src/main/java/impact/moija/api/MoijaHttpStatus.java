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

    /**
     * 401
     */
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "허가되지 않은 접근입니다."),
    INVALID_TOKEN(40101, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    EXPIRED_TOKEN(40102, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_EMAIL_OR_PASSWORD(401, HttpStatus.UNAUTHORIZED, "이메일, 패스워드를 확인하세요"),

    /**
     * 403
     */
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ;

    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String message;
}
