package impact.moija.repository.user;

import impact.moija.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User as u " +
            "join RefreshToken as r " +
            "on r.user = u " +
            "where r.token = :refreshToken")
    Optional<User> findByRefreshToken(@Param("refreshToken") String refreshToken);
}
