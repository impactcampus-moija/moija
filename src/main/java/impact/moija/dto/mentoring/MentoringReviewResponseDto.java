package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentoringReview;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringReviewResponseDto {
    private Long id;
    private boolean anonymous;
    private String content;
    private MenteeDto mentee;
    private MentorDto mentor;

    @Data
    public static class MenteeDto {
        private Long id;
        private String nickname;

        public MenteeDto(Mentee mentee) {
            this.id = mentee.getId();
            this.nickname = mentee.getUser().getNickname();
        }
    }

    @Data
    public static class MentorDto {
        private Long id;
        private String name;
        private String nickname;

        public MentorDto(Mentor mentor) {
            this.id = mentor.getId();
            this.name = mentor.getName();
            this.nickname = mentor.getUser().getNickname();
        }
    }

    public static MentoringReviewResponseDto of(MentoringReview review) {
        return MentoringReviewResponseDto.builder()
                .id(review.getId())
                .mentee(new MenteeDto(review.getMentee()))
                .mentor(new MentorDto(review.getMentor()))
                .anonymous(review.isAnonymous())
                .content(review.getContent())
                .build();
    }

}
