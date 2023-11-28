package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
}
