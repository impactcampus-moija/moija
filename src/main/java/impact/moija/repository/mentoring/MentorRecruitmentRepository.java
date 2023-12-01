package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentorRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRecruitmentRepository extends JpaRepository<MentorRecruitment, Long> {
    void deleteAllByMentor(Mentor mentor);
}
