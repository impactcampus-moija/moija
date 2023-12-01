package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.Mentoring;
import impact.moija.domain.mentoring.MentoringStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringResponseDto {
    private Long id;
    private MentoringStatus status;
    private MenteeDto mentee;
    private MentorDto mentor;

    @Data
    public static class MenteeDto {
        private Long id;
        private String name;
        private String brief;
        private String content;

        public MenteeDto(Mentee mentee) {
            this.id = mentee.getId();
            this.name = mentee.getUser().getNickname();
            this.brief = mentee.getBrief();
            this.content = mentee.getContent();
        }
    }

    @Data
    public static class MentorDto {
        private Long id;
        private String name;
        private String imageUrl;

        public MentorDto(Mentor mentor) {
            this.id = mentor.getId();
            this.name = mentor.getName();
            this.imageUrl = mentor.getImageUrl();
        }
    }

    public static MentoringResponseDto of(Mentoring mentoring) {
        return MentoringResponseDto.builder()
                .id(mentoring.getId())
                .mentee(new MenteeDto(mentoring.getMentee()))
                .mentor(new MentorDto(mentoring.getMentor()))
                .status(mentoring.getStatus())
                .build();
    }
}
