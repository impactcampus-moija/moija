package impact.moija.repository.mentoring;

import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentorTag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    @Query("select distinct m from Mentor as m "
            + "join MentorRecruitment as mr "
            + "on mr.mentor = m "
            + "where (:tag is null or mr.tag = :tag) "
            + "and (m.activate = true) ")
    List<Mentor> findByTagAndActivateIsTrue(@Param("tag") MentorTag tag);

    Page<Mentor> findByBriefContainingAndActivateIsTrue(String brief, Pageable pageable);

    @Query("select m from Mentor as m "
            + "join Mentoring as mtr "
            + "on mtr.mentor = m "
            + "where mtr.mentee.user.id = :id "
            + "and m.activate = true")
    List<Mentor> findByUserId(@Param("id") Long userId);
}
