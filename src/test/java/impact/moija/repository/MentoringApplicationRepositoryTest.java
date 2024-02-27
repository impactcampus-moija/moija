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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MentoringApplicationRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MentoringApplicationRepository applicationRepository;
    @Autowired
    private MentoringRecruitmentRepository recruitmentRepository;
    @Test
    @Transactional
    public void 멘토링지원서생성() {
        // given
        List<User> users = userRepository.saveAll(
                List.of(
                        User.builder().build(),
                        User.builder().build()
                )
        );
        MentoringRecruitment recruitment = recruitmentRepository.save(MentoringRecruitment
                .builder()
                .user(users.get(0))
                .build());

        applicationRepository.save(
                MentoringApplication.builder()
                        .recruitment(recruitment)
                        .user(users.get(1))
                        .status(MentoringStatus.PENDING)
                        .build());

        // when
        MentoringApplication result = applicationRepository
                .findByUserIdAndRecruitmentId(users.get(1).getId(), recruitment.getId())
                .orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRecruitment().getId()).isEqualTo(recruitment.getId());
        assertThat(result.getUser().getId()).isEqualTo(users.get(1).getId());
    }

    @Test
    @Transactional
    public void 멘토링지원서목록조회() {
        // given
        List<User> users = userRepository.saveAll(
                List.of(
                        User.builder().build(),
                        User.builder().build(),
                        User.builder().build(),
                        User.builder().build()
                )
        );

        MentoringRecruitment recruitment = recruitmentRepository.save(MentoringRecruitment
                .builder()
                .user(users.get(0))
                .build());

        applicationRepository.saveAll(
                List.of(
                        MentoringApplication.builder().recruitment(recruitment).user(users.get(1)).status(MentoringStatus.PENDING).build(),
                        MentoringApplication.builder().recruitment(recruitment).user(users.get(2)).status(MentoringStatus.PENDING).build(),
                        MentoringApplication.builder().recruitment(recruitment).user(users.get(3)).status(MentoringStatus.REFUSE).build()
                )
        );

        // when
        List<MentoringApplication> result = applicationRepository.findByRecruitmentIdAndStatusIsPending(recruitment.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void 나의멘토링지원서목록조회() {
        // given
        List<User> users = userRepository.saveAll(
                List.of(
                        User.builder().build(),
                        User.builder().build()
                )
        );
        List<MentoringRecruitment> recruitments = recruitmentRepository.saveAll(
                List.of(
                        MentoringRecruitment.builder().user(users.get(0)).build(),
                        MentoringRecruitment.builder().user(users.get(1)).build()
                )
        );

        applicationRepository.saveAll(
                List.of(
                        MentoringApplication.builder().recruitment(recruitments.get(0)).user(users.get(0)).status(MentoringStatus.PENDING).build(),
                        MentoringApplication.builder().recruitment(recruitments.get(0)).user(users.get(1)).status(MentoringStatus.PENDING).build(),
                        MentoringApplication.builder().recruitment(recruitments.get(1)).user(users.get(0)).status(MentoringStatus.PENDING).build()
                )
        );

        // when
        List<MentoringApplication> result = applicationRepository.findByUserId(users.get(0).getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}