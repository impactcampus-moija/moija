package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentor;
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
    private String brief;
    private long matchingCount;
    private List<String> tags;

    public static MentorListResponseDto of (Mentor mentor, long matchingCount) {
        return MentorListResponseDto.builder()
                .id(mentor.getId())
                .imageUrl(mentor.getImageUrl())
                .name(mentor.getName())
                .brief(mentor.getBrief())
                .matchingCount(matchingCount)
                .tags(mentor.getRecruitments()
                        .stream()
                        .map(recruitment -> recruitment.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
