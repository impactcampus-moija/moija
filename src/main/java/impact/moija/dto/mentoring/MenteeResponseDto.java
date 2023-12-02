package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.user.User;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenteeResponseDto {
    private Long id;
    private String name;
    private String email;
    private LocalDate age; // TODO : 나이 형식
    private String phone;
    private String brief;
    private String content;

    public static MenteeResponseDto of(Mentee mentee) {
        User user = mentee.getUser();
        return MenteeResponseDto.builder()
                .id(mentee.getId())
                .name(user.getNickname())
                .email(user.getEmail())
                .age(user.getBirthday())
                .phone(mentee.getPhone())
                .brief(mentee.getBrief())
                .content(mentee.getContent())
                .build();
    }
}
