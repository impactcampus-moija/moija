package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.MentoringStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {
    @Query("select me from Mentee as me "
            + "join Mentoring as mtr "
            + "on mtr.mentee = me "
            + "where mtr.mentor.user.id = :id "
            + "and mtr.status = :status")
    List<Mentee> findByUserId(@Param("id") Long userId,
                              @Param("status") MentoringStatus status);
}
