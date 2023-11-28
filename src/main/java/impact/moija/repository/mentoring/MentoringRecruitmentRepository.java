package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentoringRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoringRecruitmentRepository extends JpaRepository<MentoringRecruitment, Long> {
    public void deleteAllByMentor(Mentor mentor);
}
