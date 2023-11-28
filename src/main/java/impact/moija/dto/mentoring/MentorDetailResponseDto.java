package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentor;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentorDetailResponseDto {
    private Long id;
    private String imageUrl;
    private String name;
    private String brief;
    private String introduction;
    private String career;
    private List<String> tags;

    public static MentorDetailResponseDto of(Mentor mentor,String url) {
        return MentorDetailResponseDto.builder()
                .id(mentor.getId())
                .imageUrl(url)
                .name(mentor.getUser().getNickname())
                .brief(mentor.getBrief())
                .introduction(mentor.getIntroduction())
                .career(mentor.getCareer())
                .tags(mentor.getRecruitments()
                        .stream()
                        .map(recruitment -> recruitment.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
