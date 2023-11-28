package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.MentoringTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoringTagRepository extends JpaRepository<MentoringTag, Long> {
    Optional<MentoringTag> findByName(String name);
}
