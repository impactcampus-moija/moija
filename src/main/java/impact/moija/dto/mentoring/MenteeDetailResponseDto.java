package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.user.User;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenteeDetailResponseDto {
    private Long id;
    private String name;
    private String email;
    private LocalDate age;
    private String phone;
    private String brief;
    private String content;

    public static MenteeDetailResponseDto of(Mentee mentee, User user) {
        return MenteeDetailResponseDto.builder()
                .id(mentee.getId())
                .name(user.getNickname())
                .email(user.getEmail())
                .age(user.getBirthDay())
                .phone(mentee.getPhone())
                .brief(mentee.getBrief())
                .content(mentee.getContent())
                .build();
    }
}
