package impact.moija.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import impact.moija.domain.mentoring.MentoringApplication;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import impact.moija.repository.mentoring.MentoringApplicationRepository;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import impact.moija.repository.user.UserRepository;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class MentoringApplicationRepositoryTest {
    private final Long USER_ID = 1L;
    private final Long RECRUITMENT_ID = 1L;
    private final Long APPLICATION_ID = 1L;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MentoringApplicationRepository applicationRepository;
    @Autowired
    private MentoringRecruitmentRepository recruitmentRepository;
    @Test
    public void 멘토링지원서생성() {
        // given
        User user = userRepository.save(
                User.builder().id(USER_ID).build()
        );
        MentoringRecruitment recruitment = recruitmentRepository.save(
                MentoringRecruitment.builder().id(RECRUITMENT_ID).build()
        );
        applicationRepository.save(
                MentoringApplication.builder().id(APPLICATION_ID).recruitment(recruitment).user(user).build()
        );

        // when
        MentoringApplication result = applicationRepository
                .findByUserIdAndRecruitmentId(USER_ID, RECRUITMENT_ID)
                .orElse(null);
        // then
        assertThat(result).isNotNull();
    }

    @Test
    public void 멘토링지원서목록조회() {
        // given
        MentoringRecruitment recruitment = recruitmentRepository.save(
                MentoringRecruitment.builder().id(RECRUITMENT_ID).build()
        );

        applicationRepository.saveAll(
                List.of(
                        MentoringApplication.builder().id(1L).recruitment(recruitment).status(MentoringStatus.PENDING).build(),
                        MentoringApplication.builder().id(2L).recruitment(recruitment).status(MentoringStatus.PENDING).build(),
                        MentoringApplication.builder().id(3L).recruitment(recruitment).status(MentoringStatus.PROGRESS).build()
                )
        );
        // when
        List<MentoringApplication> result = applicationRepository.findByRecruitmentIdAndStatusIsPending(RECRUITMENT_ID);

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void 나의멘토링지원서목록조회() {
        // given
        List<User> user = userRepository.saveAll(
                List.of(
                        User.builder().id(1L).build(),
                        User.builder().id(2L).build()
                )
        );

        List<MentoringRecruitment> recruitment = recruitmentRepository.saveAll(
                List.of(
                        MentoringRecruitment.builder().id(1L).build(),
                        MentoringRecruitment.builder().id(2L).build()
                )
        );

        applicationRepository.saveAll(
                List.of(
                        MentoringApplication.builder().id(1L).recruitment(recruitment.get(0)).user(user.get(0)).build(),
                        MentoringApplication.builder().id(2L).recruitment(recruitment.get(0)).user(user.get(1)).build(),
                        MentoringApplication.builder().id(3L).recruitment(recruitment.get(1)).user(user.get(0)).build()
                )
        );

        // when
        List<MentoringApplication> result = applicationRepository.findByUserId(1L);

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}