package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
}
