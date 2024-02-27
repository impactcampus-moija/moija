package impact.moija.repository;

import impact.moija.domain.policy.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    boolean existsByNumber(String number);

    @Query("SELECT p FROM Policy p " +
            "WHERE (:age IS NULL OR (p.minAge = -1 OR p.minAge >= :age) AND (p.maxAge = -1 OR p.maxAge <= :age)) " +
            "AND (:location IS NULL OR p.location = :location) " +
            "AND ( " +
            "    :searchText IS NULL OR " +
            "    LOWER(p.name) LIKE LOWER(concat('%', :searchText, '%')) OR " +
            "    LOWER(p.introduction) LIKE LOWER(concat('%', :searchText, '%')) OR " +
            "    LOWER(p.content) LIKE LOWER(concat('%', :searchText, '%')) " +
            ")")
    Page<Policy> findByFilter(
            @Param("age") Integer age,
            @Param("location") String location,
            @Param("searchText") String searchText,
            Pageable pageable
    );
}
