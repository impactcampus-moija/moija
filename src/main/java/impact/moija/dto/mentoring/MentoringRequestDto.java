package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringRequestDto {
    private MentoringStatus status;
    private String reason;
}
