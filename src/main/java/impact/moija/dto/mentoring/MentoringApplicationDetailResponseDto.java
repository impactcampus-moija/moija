package impact.moija.dto.mentoring;

import impact.moija.domain.mentoring.MentoringApplication;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import java.time.LocalDate;
import java.time.Period;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentoringApplicationDetailResponseDto {
    private Long id;
    private String name;
    private String topic;
    private String content;
    private String email;
    private String phone;
    private String age;
    private MentoringStatus status;
    private String reject;

    public static MentoringApplicationDetailResponseDto of(MentoringApplication application) {
        User user = application.getUser();
        // 만 나이 구하기
        Period period = Period.between(user.getBirthday(), LocalDate.now());
        return MentoringApplicationDetailResponseDto.builder()
                .id(application.getId())
                .name(user.getNickname())
                .topic(application.getTopic())
                .content(application.getContent())
                .email(user.getEmail())
                .phone(application.getPhone())
                .age(String.valueOf(period.getYears()))
                .status(application.getStatus())
                .reject(application.getReason())
                .build();
    }
}
