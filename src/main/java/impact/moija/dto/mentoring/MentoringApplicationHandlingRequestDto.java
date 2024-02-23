package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.validator.CheckMentoringStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentoringApplicationHandlingRequestDto {

    @CheckMentoringStatus(value = {"REFUSE", "PROGRESS"})
    private MentoringStatus status;
    private String reason;
}
