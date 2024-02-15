package impact.moija.domain.mentoring;

import impact.moija.domain.common.BaseTimeEntity;
import impact.moija.domain.user.User;
import impact.moija.dto.mentoring.MentoringApplicationRequestDto;
import impact.moija.dto.mentoring.MentoringApplicationHandlingRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "mentoring_applications")
public class MentoringApplication extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String phone;

    String topic;

    @Column(columnDefinition = "TEXT")
    String content;

    MentoringStatus status;

    @Column(columnDefinition = "TEXT")
    String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    MentoringRecruitment recruitment;

    public void updateApplication(MentoringApplicationRequestDto dto) {
        this.phone = dto.getPhone() == null ? this.phone : dto.getPhone();
        this.topic = dto.getTopic() == null ? this.topic : dto.getTopic();
        this.content = dto.getContent() == null ? this.content : dto.getContent();
    }

    public void handleApplication(MentoringApplicationHandlingRequestDto dto) {
        this.reason = dto.getReason() == null ? this.reason : dto.getReason();
        this.status = dto.getStatus() == null ? this.status : dto.getStatus();
    }
}
