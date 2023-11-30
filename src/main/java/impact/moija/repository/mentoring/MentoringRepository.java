package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.Mentoring;
import impact.moija.domain.mentoring.MentoringStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
    void deleteAllByMentee(Mentee mentee);

    @Query("select CASE WHEN COUNT(mtr) > 0 THEN true ELSE false END "
            + "from Mentoring as mtr "
            + "where mtr.mentor = :mentor "
            + "and mtr.mentee.user.id = :id " )
    boolean existsByMentorAndUserId(@Param("mentor") Mentor mentor,
                                    @Param("id") Long userId);

    @Query("select mtr from Mentoring as mtr "
            + "where mtr.mentor.user.id = :id "
            + "and mtr.status = :status")
    List<Mentoring> findByUserIdAndStatus(@Param("id") Long userId,
                                          @Param("status") MentoringStatus status);

    @Query("select mtr from Mentoring as mtr "
            + "where mtr.mentee.user.id = :id "
            + "and mtr.status <> :status")
    List<Mentoring> findByUserIdExceptStatus(@Param("id") Long userId,
                                             @Param("status") MentoringStatus status);

    @Query("select count(mtr) from Mentoring as mtr "
            + "where mtr.mentor = :mentor "
            + "and (mtr.status = :first or mtr.status = :second) ")
    long countMatchingMentor(@Param("mentor") Mentor mentor,
                             @Param("first") MentoringStatus first,
                             @Param("second") MentoringStatus second);
}
