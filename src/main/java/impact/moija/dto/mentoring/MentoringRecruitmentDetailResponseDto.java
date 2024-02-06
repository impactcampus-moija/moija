package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringRecruitment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringRecruitmentDetailResponseDto {
    private Long id;
    private String category;
    private String imageUrl;
    private String name;
    private String brief;
    private String introduction;
    private String career;
    private boolean activate;

    public static MentoringRecruitmentDetailResponseDto of (MentoringRecruitment recruitment) {
        return MentoringRecruitmentDetailResponseDto.builder()
                .id(recruitment.getId())
                .category(recruitment.getCategory())
                .imageUrl(recruitment.getCategory())
                .name(recruitment.getName())
                .brief(recruitment.getBrief())
                .introduction(recruitment.getIntroduction())
                .career(recruitment.getCareer())
                .activate(recruitment.isActivate())
                .build();
    }
}
