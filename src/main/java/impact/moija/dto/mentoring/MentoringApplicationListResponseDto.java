package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringApplication;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.mentoring.MentoringStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringApplicationListResponseDto {
    private Long id;
    private String name;
    private String topic;
    private MentoringStatus status;

    public static MentoringApplicationListResponseDto of (MentoringApplication application) {
        return MentoringApplicationListResponseDto.builder()
                .id(application.getId())
                .name(application.getUser().getNickname())
                .topic(application.getTopic())
                .status(application.getStatus())
                .build();
    }
}
