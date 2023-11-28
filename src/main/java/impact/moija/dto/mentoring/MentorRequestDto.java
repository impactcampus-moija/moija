package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.user.User;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentorRequestDto {
    String occupation;
    String brief;
    String introduction;
    String career;
    String phone;
    List<String> tags;

    public Mentor toEntity(User user, boolean activate) {
        return Mentor.builder()
                .occupation(this.occupation)
                .brief(this.brief)
                .introduction(this.introduction)
                .career(this.career)
                .phone(this.phone)
                .activate(activate)
                .user(user)
                .build();
    }
}
