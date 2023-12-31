package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentor;
import impact.moija.dto.common.RecommendationResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentorListResponseDto {
    private Long id;
    private String imageUrl;
    private String name;
    private String nickname;
    private String brief;
    private long matchingCount;
    private long reviewCount;
    private RecommendationResponseDto recommendation;
    private List<String> tags;

    public static MentorListResponseDto of (Mentor mentor,
                                            long matchingCount,
                                            long reviewCount,
                                            RecommendationResponseDto recommendation) {
        return MentorListResponseDto.builder()
                .id(mentor.getId())
                .imageUrl(mentor.getImageUrl())
                .name(mentor.getName())
                .nickname(mentor.getUser().getNickname())
                .brief(mentor.getBrief())
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
