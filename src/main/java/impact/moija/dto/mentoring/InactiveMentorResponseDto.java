package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentor;
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

    public static InactiveMentorResponseDto of(final Mentor mentor) {
        return InactiveMentorResponseDto.builder()
                .id(mentor.getId())
                .imageUrl(mentor.getImageUrl())
                .name(mentor.getName())
                .brief(mentor.getBrief())
                .introduction(mentor.getIntroduction())
                .career(mentor.getCareer())
                .build();
    }
}
