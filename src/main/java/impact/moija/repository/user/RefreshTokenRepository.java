package impact.moija.repository.user;

import impact.moija.domain.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(final String token);
    Optional<RefreshToken> findByUserId(final Long userId);
}
