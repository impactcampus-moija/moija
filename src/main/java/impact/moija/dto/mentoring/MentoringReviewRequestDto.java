package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentoringReview;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringReviewRequestDto {
    private boolean anonymous;
    private String content;

    public MentoringReview toEntity(Mentor mentor, Mentee mentee) {
        return MentoringReview.builder()
                .mentor(mentor)
                .mentee(mentee)
                .anonymous(this.anonymous)
                .content(this.content)
                .build();
    }
}
