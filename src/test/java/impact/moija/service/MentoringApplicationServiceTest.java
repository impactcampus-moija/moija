package impact.moija.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.MentoringApplication;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationDetailResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationListResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationRequestDto;
import impact.moija.dto.mentoring.MentoringApplicationHandlingRequestDto;
import impact.moija.repository.mentoring.MentoringApplicationRepository;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MentoringApplicationServiceTest {

    private final Long USER_ID = 1L;
    private final Long RECRUITMENT_ID = 1L;
    private final Long APPLICATION_ID = 1L;
    @InjectMocks
    private MentoringApplicationService target;
    @Mock
    private MentoringApplicationRepository applicationRepository;
    @Mock
    private MentoringRecruitmentRepository recruitmentRepository;
    @Mock
    private UserService userService;

    @Test
    public void 멘토링지원서등록실패_중복지원() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(MentoringApplication.builder().build()))
                .when(applicationRepository)
                .findByUserIdAndRecruitmentId(anyLong(), anyLong());

        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.createMentoringApplication(RECRUITMENT_ID, createDto()));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.DUPLICATE_MENTORING_APPLICATION);
    }

    @Test
    public void 멘토링지원서등록실패_모집서작성자는지원불가() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(MentoringRecruitment.builder().id(RECRUITMENT_ID).build()))
                .when(recruitmentRepository)
                .findByUserId(anyLong());

        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.createMentoringApplication(RECRUITMENT_ID, createDto()));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.INVALID_MENTORING_APPLICATION);
    }

    @Test
    public void 멘토링지원서등록성공() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.empty())
                .when(applicationRepository)
                .findByUserIdAndRecruitmentId(anyLong(), anyLong());
        doReturn(Optional.empty())
                .when(recruitmentRepository)
                .findByUserId(anyLong());
        doReturn(MentoringApplication.builder().id(APPLICATION_ID).build())
                .when(applicationRepository)
                .save(any(MentoringApplication.class));

        // when
        final PkResponseDto result = target.createMentoringApplication(RECRUITMENT_ID, createDto());

        // then
        assertThat(result).isNotNull();
    }

    @Test
    public void 멘토링지원서목록조회실패_멘토링모집서없음() {
        // given
        doReturn(Optional.empty())
                .when(recruitmentRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.getPendingMentoringApplications(RECRUITMENT_ID));
        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT);
    }

    @Test
    public void 멘토링지원서목록조회실패_본인멘토링모집서가아님() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();

        doReturn(Optional.of(MentoringRecruitment
            .builder()
            .user(User.builder().id(2L).build())
            .build()))
                .when(recruitmentRepository)
                .findById(anyLong());

        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.getPendingMentoringApplications(RECRUITMENT_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }

    @Test
    public void 대기멘토링지원서목록조회성공() {
        // given
        doReturn(Optional.of(MentoringRecruitment.builder().id(RECRUITMENT_ID).build()))
                .when(recruitmentRepository)
                .findById(RECRUITMENT_ID);
        doReturn(
                List.of(
                        MentoringApplication.builder().build(),
                        MentoringApplication.builder().build(),
                        MentoringApplication.builder().build()
                        )
        )
                .when(applicationRepository)
                .findByRecruitmentIdAndStatusIsPending(anyLong());

        // when
        List<MentoringApplicationListResponseDto> result = target.getPendingMentoringApplications(RECRUITMENT_ID);

        // then
        assertThat(result.size()).isEqualTo(3);
    }

    private MentoringApplicationRequestDto createDto() {
        return MentoringApplicationRequestDto.builder()
                .phone("01012345678")
                .topic("토픽")
                .content("내용")
                .build();
    }

    private MentoringApplicationRequestDto updateDto() {
        return MentoringApplicationRequestDto.builder()
                .phone("01098765432")
                .topic("토픽1")
                .content("내용1")
                .build();
    }

    @Test
    public void 멘토링지원서상세조회실패_멘토링모집서없음() {
        // given
        doReturn(Optional.empty())
                .when(recruitmentRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.getMentoringApplication(RECRUITMENT_ID, APPLICATION_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT);
    }

    @Test
    public void 멘토링지원서상세조회실패_멘토링지원서없음() {
        // given
        doReturn(Optional.empty())
                .when(applicationRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.getMentoringApplication(RECRUITMENT_ID, APPLICATION_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.NOT_FOUND_MENTORING_APPLICATION);
    }

    @Test
    public void 멘토링지원서상세조회실패_권한없음() {
        // given
        doReturn(1L)
                .when(userService)
                .getLoginMemberId();

        doReturn(Optional.of(MentoringRecruitment.builder()
            .user(User.builder().id(3L).build())
            .build()))
                .when(recruitmentRepository)
                .findById(anyLong());

        doReturn(Optional.of(MentoringApplication.builder()
                .user(User.builder().id(2L).build())
                .build()))
                .when(applicationRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.getMentoringApplication(RECRUITMENT_ID, APPLICATION_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }

    @Test
    public void 멘토링지원서상세조회성공() {
        // given
        doReturn(Optional.of(MentoringApplication.builder()
            .id(APPLICATION_ID)
            .user(User.builder().id(USER_ID).birthday(LocalDate.now()).build())
            .build()))
                .when(applicationRepository)
                .findById(anyLong());

        // when
        final MentoringApplicationDetailResponseDto result = target
                .getMentoringApplication(RECRUITMENT_ID, APPLICATION_ID);

        // then
        assertThat(result.getId()).isEqualTo(APPLICATION_ID);
    }

    @Test
    public void 멘토링지원서수정실패_본인아님() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();

        doReturn(Optional.of(MentoringApplication.builder()
            .user(User.builder().id(2L).build())
            .build()))
                .when(applicationRepository)
                .findById(anyLong());

        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.updateMentoringApplication(anyLong(), any(MentoringApplicationRequestDto.class)));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }

    @Test
    public void 멘토링지원서수정성공() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();

        doReturn(Optional.of(MentoringApplication.builder()
            .id(APPLICATION_ID)
            .phone("01012345678")
            .topic("토픽")
            .content("내용")
            .user(User.builder().id(USER_ID).build())
            .build()))
                .when(applicationRepository)
                .findById(anyLong());

        // when
        PkResponseDto result = target.updateMentoringApplication(APPLICATION_ID, updateDto());

        // then
        assertThat(result.getId()).isEqualTo(APPLICATION_ID);
    }

    @Test
    public void 멘토링모집서삭제실패_본인아님() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();

        doReturn(Optional.of(
                MentoringApplication.builder()
                        .user(User.builder().id(-1L).build())
                        .build()))
                .when(applicationRepository)
                .findById(anyLong());

        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.deleteMentoringApplication(RECRUITMENT_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }

    @Test
    public void 멘토링지원서삭제실패_존재하지않음() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.empty())
                .when(applicationRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.deleteMentoringApplication(RECRUITMENT_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.NOT_FOUND_MENTORING_APPLICATION);
    }

    @Test
    public void 멘토링지원서삭제성공() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();

        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .user(User.builder().id(USER_ID).build())
                        .build()))
                .when(applicationRepository)
                .findById(anyLong());
        // when
        target.deleteMentoringApplication(RECRUITMENT_ID);

        // then
    }

    @Test
    public void 멘토링지원서처리실패_본인아님() {
        // given
        doReturn(Optional.of(MentoringRecruitment.builder()
            .user(User.builder().id(2L).build())
            .build()))
                .when(recruitmentRepository)
                .findById(anyLong());

        doReturn(Optional.of(MentoringApplication.builder()
            .build()))
                .when(applicationRepository)
                .findById(anyLong());

        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();

        MentoringApplicationHandlingRequestDto handling = MentoringApplicationHandlingRequestDto.builder()
                .reason("이러이러한 이유로 죄송하게 되었습니다")
                .build();

        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.handleMentoringApplication(RECRUITMENT_ID, APPLICATION_ID, handling));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }

    @Test
    public void 멘토링지원서처리성공() {
        // given
        doReturn(Optional.of(MentoringRecruitment.builder()
                .user(User.builder().id(USER_ID).build())
                .build()))
                .when(recruitmentRepository)
                .findById(anyLong());

        doReturn(Optional.of(MentoringApplication.builder()
                .build()))
                .when(applicationRepository)
                .findById(anyLong());

        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();

        MentoringApplicationHandlingRequestDto handle = MentoringApplicationHandlingRequestDto.builder()
                .status(MentoringStatus.REFUSE)
                .reason("이러이러한 이유로 죄송하게 되었습니다")
                .build();
        // when
        final PkResponseDto result = target.handleMentoringApplication(RECRUITMENT_ID, APPLICATION_ID, handle);

        // then
        assertThat(result.getId()).isEqualTo(RECRUITMENT_ID);
    }

}
