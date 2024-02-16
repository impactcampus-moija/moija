package impact.moija.domain.mentoring;

import impact.moija.domain.common.BaseTimeEntity;
import impact.moija.domain.user.User;
import impact.moija.dto.mentoring.MentoringRecruitmentRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mentoring_recruitments")
public class MentoringRecruitment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String category;

    String name;

    String imageUrl;

    String brief;
    @Column(columnDefinition = "TEXT")
    String introduction;
    @Column(columnDefinition = "TEXT")
    String career;

    String occupation;

    boolean activate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateRecruitment(MentoringRecruitmentRequestDto dto) {
        this.category = dto.getCategory() == null ? this.category : dto.getCategory();
        this.brief = dto.getBrief() == null ? this.brief : dto.getBrief();
        this.introduction = dto.getIntroduction() == null ?  this.introduction : dto.getIntroduction();
        this.career = dto.getCareer() == null ? this.career : dto.getCareer();
        this.occupation = dto.getOccupation() == null ? this.occupation : dto.getOccupation();
        this.name = dto.getName() == null ? this.name : dto.getName();
    }

    public void convertActivate() {
        this.activate = !this.activate;
    }
}
