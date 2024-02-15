package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.MentoringRecruitment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MentoringRecruitmentRepository extends JpaRepository<MentoringRecruitment, Long> {
    @Query("select m from MentoringRecruitment m "
            + "where m.user.id = :userId")
    Optional<MentoringRecruitment> findByUserId(@Param("userId") Long userId);

    @Query("select m from MentoringRecruitment m "
            + "where (m.activate = true) "
            + "and (:category is null or m.category like %:category%) "
            + "and (:keyword is null or m.brief like %:keyword%) ")
    Page<MentoringRecruitment> findByCategoryAndKeyword(Pageable pageable,
                                                        @Param("category") String category,
                                                        @Param("keyword") String keyword);

    @Query("select count(m) from MentoringRecruitment m "
            + "join fetch MentorRecommendation mr on m = mr.recruitment "
            + "where m.id = :recruitmentId ")
    long countRecommendation(@Param("recruitmentId") Long recruitmentId);
}
