package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentoringReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoringReviewRepository extends JpaRepository<MentoringReview, Long> {
    List<MentoringReview> findByMentor(Mentor mentor);
    long countMentoringReviewByMentor(Mentor mentor);
}
