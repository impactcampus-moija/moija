package impact.moija.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final MoijaHttpStatus status;
}
