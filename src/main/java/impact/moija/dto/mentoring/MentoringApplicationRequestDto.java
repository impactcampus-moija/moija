package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringApplication;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentoringApplicationRequestDto {
    @Length(min = 11, max = 11)
    private String phone;
    @NotNull
    private String topic;
    @NotNull
    private String content;

    public MentoringApplication toEntity(Long userId, Long recruitmentId, MentoringStatus status) {
        return MentoringApplication.builder()
                .phone(this.phone)
                .topic(this.topic)
                .content(this.content)
                .status(status)
                .recruitment(MentoringRecruitment.builder().id(recruitmentId).build())
                .user(User.builder().id(userId).build())
                .build();
    }
}
