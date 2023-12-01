package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentor;
import impact.moija.dto.common.RecommendationResponseDto;
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
    private String nickname;
    private String brief;
    private String introduction;
    private String career;
    private long matchingCount;
    private long reviewCount;
    private RecommendationResponseDto recommendation;
    private boolean activate;
    private List<String> tags;

    public static MentorDetailResponseDto of(Mentor mentor,
                                             String url,
                                             long matchingCount,
                                             long reviewCount,
                                             RecommendationResponseDto recommendation) {
        return MentorDetailResponseDto.builder()
                .id(mentor.getId())
                .imageUrl(url)
                .name(mentor.getName())
                .nickname(mentor.getUser().getNickname())
                .brief(mentor.getBrief())
                .introduction(mentor.getIntroduction())
                .career(mentor.getCareer())
                .activate(mentor.isActivate())
                .matchingCount(matchingCount)
                .reviewCount(reviewCount)
                .recommendation(recommendation)
                .tags(mentor.getRecruitments()
                        .stream()
                        .map(recruitment -> recruitment.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
