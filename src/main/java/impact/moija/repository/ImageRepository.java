package impact.moija.repository;

import impact.moija.domain.common.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByTargetTableAndTargetId(String table, Long id);
}
