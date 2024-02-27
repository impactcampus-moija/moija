package impact.moija.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import impact.moija.domain.mentoring.MentorRecommendation;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.user.User;
import impact.moija.repository.mentoring.MentorRecommendationRepository;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import impact.moija.repository.user.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MentoringRecruitmentRepositoryTest {
    @Autowired
    private MentoringRecruitmentRepository recruitmentRepository;
    @Autowired
    private MentorRecommendationRepository recommendationRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 멘토링_모집서_생성() {
        // given
        final User user = userRepository.save(User.builder().build());
        final MentoringRecruitment recruitment = MentoringRecruitment.builder()
                .user(user)
                .build();

        // when
        final MentoringRecruitment result = recruitmentRepository.save(recruitment);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void 멘토링_모집서를_userID로조회() {

        // given
        final User user = userRepository.save(User.builder().build()); // 맞게 테스트 하는건지 모르겠음.
        final MentoringRecruitment recruitment = MentoringRecruitment.builder()
                .user(user)
                .build();
        // when
        recruitmentRepository.save(recruitment);
        final MentoringRecruitment result = recruitmentRepository.findByUserId(user.getId()).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void 활성화_멘토링모집서_목록을_Pagination조회() {
        // given
        final List<MentoringRecruitment> recruitments = recruitmentRepository.saveAll(List.of(
                MentoringRecruitment.builder()
                        .category("일자리,주거")
                        .name("멘토1")
                        .brief("구직활동에 도움을 드릴게요")
                        .activate(true)
                        .build(),
                MentoringRecruitment.builder()
                        .category("일자리,주거")
                        .name("멘토1")
                        .brief("취직하고 싶으신 분")
                        .activate(true)
                        .build(),
                MentoringRecruitment.builder()
                        .category("주거")
                        .name("멘토1")
                        .brief("부동산 서류 작성 도움을 드릴게요")
                        .activate(true)
                        .build(),
                MentoringRecruitment.builder()
                        .category("일자리,주거")
                        .name("멘토3")
                        .brief("구직활동에 도움을 드릴게요")
                        .activate(false)
                        .build()
        ));
        Pageable pageable = PageRequest.of(0,8);
        // when

        Page<MentoringRecruitment> activate = recruitmentRepository
                .findByCategoryAndKeyword(pageable, null, null);
        Page<MentoringRecruitment> category = recruitmentRepository
                .findByCategoryAndKeyword(pageable, "일자리", null);
        Page<MentoringRecruitment> keyword = recruitmentRepository
                .findByCategoryAndKeyword(pageable, "일자리", "도움");

        // then
        assertThat(activate.getTotalElements()).isEqualTo(3);
        assertThat(category.getTotalElements()).isEqualTo(2);
        assertThat(keyword.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void 멘토링모집서추천개수조회(){
        // given
        List<User> users = userRepository.saveAll(List.of(
                User.builder().build(),
                User.builder().build(),
                User.builder().build()
        ));

        MentoringRecruitment recruitment = recruitmentRepository.save(
                MentoringRecruitment.builder().build()
        );

        for (User user : users) {
            recommendationRepository.save(MentorRecommendation.builder().user(user).recruitment(recruitment).build());
        }

        // when
        long result = recruitmentRepository.countRecommendation(recruitment.getId());

        // then
        assertThat(result).isEqualTo(3L);
    }
}

