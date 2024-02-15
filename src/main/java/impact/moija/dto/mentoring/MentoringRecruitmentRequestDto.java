package impact.moija.dto.mentoring;

import com.sun.istack.NotNull;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.user.User;
import impact.moija.validator.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentoringRecruitmentRequestDto {

    @Category
    String category;
    @NotNull
    String name;
    String brief;
    String introduction;
    String career;
    String occupation;

    public MentoringRecruitment toEntity(Long userId, boolean activate) {
        return MentoringRecruitment.builder()
                .occupation(this.occupation)
                .brief(this.brief)
                .introduction(this.introduction)
                .career(this.career)
                .name(this.name)
                .activate(activate)
                .user(User.builder().id(userId).build())
                .build();
    }
}
