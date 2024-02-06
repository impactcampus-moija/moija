package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringRecruitment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringRecruitmentListResponseDto {
    private Long id;
    private String category;
    private String imageUrl;
    private String name;
    private String brief;
    private boolean activate;

    public static MentoringRecruitmentListResponseDto of (MentoringRecruitment recruitment) {
        return MentoringRecruitmentListResponseDto.builder()
                .id(recruitment.getId())
                .category(recruitment.getCategory())
                .imageUrl(recruitment.getCategory())
                .name(recruitment.getName())
                .brief(recruitment.getBrief())
                .activate(recruitment.isActivate())
                .build();
    }
}
