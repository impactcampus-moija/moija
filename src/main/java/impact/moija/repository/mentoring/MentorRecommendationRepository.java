package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.MentorRecommendation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRecommendationRepository extends JpaRepository<MentorRecommendation, Long> {
    Optional<MentorRecommendation> findByMentorIdAndUserId(Long mentorId, Long userId);
}
