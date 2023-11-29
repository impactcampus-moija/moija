package impact.moija.dto.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PkResponseDto {
    Long id;

    public static PkResponseDto of(Long id) {
        return new PkResponseDto(id);
    }
}
