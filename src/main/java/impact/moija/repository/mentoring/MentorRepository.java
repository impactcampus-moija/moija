package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentoringTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByActivateIsTrue();
    @Query("select m from Mentor as m "
            + "join MentoringRecruitment as mr "
            + "on mr.mentor = m "
            + "where mr.tag = :tag and m.activate = true")
    List<Mentor> findByTagAndActivateIsTrue(@Param("tag") MentoringTag tag);
}
