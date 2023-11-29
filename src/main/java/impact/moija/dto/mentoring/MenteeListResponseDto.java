package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenteeListResponseDto {
    private Long id;
    private String name;
    private String brief;
    private String content;
    private MentoringStatus status;

    public static MenteeListResponseDto of(Mentee mentee, User user) {
        return MenteeListResponseDto.builder()
                .id(mentee.getId())
                .name(user.getNickname())
                .brief(mentee.getBrief())
                .content(mentee.getContent())
                .status(mentee.getStatus())
                .build();
    }
}
