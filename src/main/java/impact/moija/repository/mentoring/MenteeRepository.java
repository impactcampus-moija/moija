package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {
}
