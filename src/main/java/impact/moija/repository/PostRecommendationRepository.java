package impact.moija.repository;

import impact.moija.domain.community.Post;
import impact.moija.domain.community.PostRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRecommendationRepository extends JpaRepository<PostRecommendation, Long> {
    Optional<PostRecommendation> findByUserIdAndPostId(Long loginUserId, Long postId);
}
