package impact.moija.dto.mentoring;

import impact.moija.domain.user.Independence;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InactiveIndependenceResponseDto {
    private Long id;
    private String nickName;
    private int independenceYear;

    public static InactiveIndependenceResponseDto of(final Independence independence) {
        return InactiveIndependenceResponseDto.builder()
                .id(independence.getId())
                .nickName(independence.getUser().getNickname())
                .independenceYear(independence.getIndependenceYear())
                .build();
    }
}
