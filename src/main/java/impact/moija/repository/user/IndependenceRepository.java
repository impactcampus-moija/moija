package impact.moija.repository.user;

import impact.moija.domain.user.Independence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndependenceRepository extends JpaRepository<Independence, Long> {
    Optional<Independence> findByUserId(Long userId);

    Page<Independence> findByActivateIsFalse(Pageable pageable);
}
