package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenteeRequestDto {
    private String phone;
    private String brief;
    private String content;

    public Mentee toEntity(User user) {
        return Mentee.builder()
                .phone(this.phone)
                .brief(this.brief)
                .content(this.content)
                .user(user)
                .build();
    }
}
