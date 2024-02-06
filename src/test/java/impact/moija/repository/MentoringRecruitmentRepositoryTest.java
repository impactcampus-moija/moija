package impact.moija.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.user.User;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import impact.moija.repository.user.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class MentoringRecruitmentRepositoryTest {
    private final Long USER_ID = 1L;
    @Autowired
    private MentoringRecruitmentRepository target;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 멘토링_모집서_생성() {
        // given
        final User user = userRepository.save(User.builder().id(USER_ID).build());
        final MentoringRecruitment recruitment = mentoringRecruitment(user);

        // when
        final MentoringRecruitment result = target.save(recruitment);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(recruitment.getName());
        assertThat(result.getUser().getEmail()).isEqualTo(recruitment.getUser().getEmail());
    }

    @Test
    public void 멘토링_모집서를_userID로조회() {

        // given
        final User user = userRepository.save(User.builder().id(USER_ID).build()); // 맞게 테스트 하는건지 모르겠음.
        final MentoringRecruitment recruitment = mentoringRecruitment(user);

        // when
        target.save(recruitment);
        final MentoringRecruitment result = target.findByUserId(USER_ID).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUser()).isNotNull();
    }

    @Test
    public void 활성화_멘토링모집서_목록을_Pagination조회() {
        // given
        final List<MentoringRecruitment> recruitments = List.of(
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
        );
        List<MentoringRecruitment> save = target.saveAll(recruitments);

        Pageable pageable = PageRequest.of(0,8);
        // when

        Page<MentoringRecruitment> result = target.findByCategoryAndKeyword(pageable, "일자리", "도움");

        // then
        assertThat(result.getContent().size()).isEqualTo(1);
        for(MentoringRecruitment recruitment : result.getContent()) {
            assertThat(recruitment.getCategory().contains("일자리")).isTrue();
            assertThat(recruitment.getBrief().contains("도움")).isTrue();
        }
    }


    private MentoringRecruitment mentoringRecruitment(User user) {
        return MentoringRecruitment.builder()
                .category("일자리,주거")
                .name("멘토1")
                .brief("간략한 소개1")
                .activate(true)
                .user(user)
                .build();
    }
}

