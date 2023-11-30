package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.MentorTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorTagRepository extends JpaRepository<MentorTag, Long> {
    Optional<MentorTag> findByName(String name);
}
