package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringRecruitment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InactiveMentorResponseDto {
    private Long id;
    private String imageUrl;
    private String name;
    private String brief;
    private String introduction;
    private String career;
    private List<String> tags;

    public static InactiveMentorResponseDto of(final MentoringRecruitment recruitment) {
        return InactiveMentorResponseDto.builder()
                .id(recruitment.getId())
                .imageUrl(recruitment.getImageUrl())
                .name(recruitment.getName())
                .brief(recruitment.getBrief())
                .introduction(recruitment.getIntroduction())
                .career(recruitment.getCareer())
                .build();
    }
}
