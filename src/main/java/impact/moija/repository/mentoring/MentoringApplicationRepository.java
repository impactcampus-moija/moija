package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.MentoringApplication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MentoringApplicationRepository extends JpaRepository<MentoringApplication, Long> {

    @Query("select m from MentoringApplication m"
            + " where m.user.id = :userId "
            + " and m.recruitment.id = :recruitmentId")
    Optional<MentoringApplication> findByUserIdAndRecruitmentId(@Param("userId") Long userId,
                                                                @Param("recruitmentId") Long recruitmentId);

    @Query("select m from MentoringApplication m "
            + "where m.recruitment.id = :recruitmentId "
            + "and m.status = impact.moija.domain.mentoring.MentoringStatus.PENDING")
    List<MentoringApplication> findByRecruitmentIdAndStatusIsPending(@Param("recruitmentId") Long recruitmentId);
}
