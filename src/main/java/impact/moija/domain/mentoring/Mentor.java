package impact.moija.domain.mentoring;

import impact.moija.domain.common.BaseTimeEntity;
import impact.moija.domain.user.User;
import impact.moija.dto.mentoring.MentorRequestDto;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "mentors")
public class Mentor extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String occupation;

    String brief;

    String introduction;

    String career;

    String phone;

    boolean activate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "mentor")
    List<MentoringRecruitment> recruitments;

    public void updateMentor(MentorRequestDto dto) {
        this.occupation = dto.getOccupation() != null ? dto.getOccupation() : this.occupation;
        this.brief = dto.getBrief() != null ? dto.getBrief() : this.brief;
        this.introduction = dto.getIntroduction() != null ? dto.getIntroduction() : this.introduction;
        this.career = dto.getCareer() != null ? dto.getCareer() : this.career;
        this.phone = dto.getPhone() != null ? dto.getPhone() : this.phone;
    }

    public void updateActivate(boolean activate) {
        this.activate = activate;
    }
}
