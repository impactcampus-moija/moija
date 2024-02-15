package impact.moija.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.MentorRecommendation;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.user.User;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.common.RecommendationResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentDetailResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentListResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentRequestDto;
import impact.moija.repository.mentoring.MentorRecommendationRepository;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class MentoringRecruitmentServiceTest {
    private final Long USER_ID = 1L;
    private final Long RECRUITMENT_ID = 1L;
    @InjectMocks
    private MentoringRecruitmentService target;
    @Mock
    private MentoringRecruitmentRepository mentoringRecruitmentRepository;
    @Mock
    private MentorRecommendationRepository mentorRecommendationRepository;
    @Mock
    private UserService userService;
    @Mock
    private ImageService imageService;

    private final MockMultipartFile image = new MockMultipartFile("image",
            "image.jpg",
            "multipart/form-data",
            "image".getBytes(StandardCharsets.UTF_8)
    );

    @Test
    public void 멘토링모집서등록실패_이미존재() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(MentoringRecruitment.builder().build()))
                .when(mentoringRecruitmentRepository)
                .findByUserId(anyLong());

        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.createMentoringRecruitment(createDto(), image));
        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.DUPLICATE_MENTORING_RECRUITMENT);
    }

    @Test
    public void 멘토링모집서등록성공() {

        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.empty())
                .when(mentoringRecruitmentRepository)
                .findByUserId(anyLong());
        doReturn(MentoringRecruitment.builder().id(RECRUITMENT_ID).build())
                .when(mentoringRecruitmentRepository)
                .save(any(MentoringRecruitment.class));
        doReturn("image")
                .when(imageService)
                .createImage(anyString(), anyLong(), any(MockMultipartFile.class));
        // when
        final PkResponseDto result = target.createMentoringRecruitment(createDto(), image);

        // verify
        verify(mentoringRecruitmentRepository, times(1)).findByUserId(anyLong());
        verify(mentoringRecruitmentRepository, times(1)).save(any(MentoringRecruitment.class));

        //then
        // Auto Increment의 문제?
        assertThat(result).isNotNull();
    }

    @Test
    public void 멘토링모집서목록조회성공_페이징() {
        // given
        Pageable pageable = PageRequest.of(0, 8);

        doReturn(new PageImpl<>(
                List.of(
                        MentoringRecruitment.builder().category("일자리,주거").brief("구직활동에 도움을 드릴게요").activate(true).build(),
                        MentoringRecruitment.builder().category("일자리,주거").brief("취직하고 싶으신 분").activate(true).build(),
                        MentoringRecruitment.builder().category("주거").brief("부동산 서류 작성 도움을 드릴게요").activate(true).build()
                ), pageable, 4)
        )
                .when(mentoringRecruitmentRepository)
                .findByCategoryAndKeyword(
                    any(PageRequest.class),
                    anyString(),
                    anyString());

        // when
        final PageResponse<MentoringRecruitmentListResponseDto> result = target.getMentoringRecruitments(
                pageable,
                "",
                "");

        // then
        assertThat(result.getItemList().size()).isEqualTo(3);
    }

    @Test
    public void 멘토링모집서상세조회실패_존재하지않음() {
        // given
        doReturn(Optional.empty())
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.getMentoringRecruitment(RECRUITMENT_ID));
        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT);
    }

    @Test
    public void 멘토링모집서상세조회성공() {
        // given
        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .category("일자리,주거")
                        .brief("구직활동에 도움을 드릴게요")
                        .activate(true)
                        .build()
        ))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        final MentoringRecruitmentDetailResponseDto result = target.getMentoringRecruitment(1L);

        // then
        assertThat(result.getCategory()).isEqualTo("일자리,주거");
    }

    @Test
    public void 멘토링모집서수정실패_본인이아님() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .user(User.builder().id(-1L).build())
                        .build()))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.updateMentoringRecruitment(updateDto(), image, RECRUITMENT_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }

    @Test
    public void 멘토링모집서수정성공() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .category("일자리,주거")
                        .brief("도와드릴게요")
                        .imageUrl("image1")
                        .name("멘토1")
                        .user(User.builder().id(USER_ID).build())
                        .build()
        ))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        doReturn("image2")
                .when(imageService)
                .createImage(anyString(), anyLong(), any(MockMultipartFile.class));
        MentoringRecruitmentRequestDto recruitment = updateDto();

        // when
        PkResponseDto result = target.updateMentoringRecruitment(recruitment, image, RECRUITMENT_ID);

        // then
        verify(imageService, times(1)).deleteImage(anyString(), anyLong());
        assertThat(result).isNotNull();
    }

    @Test
    public void 멘토링모집서활성화설정실패_본인아님() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .user(User.builder().id(-1L).build())
                        .build()))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.updateActivate(RECRUITMENT_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }

    @Test
    public void 멘토링모집서활성화설정성공() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .user(User.builder().id(USER_ID).build())
                        .activate(true)
                        .build()))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        final PkResponseDto result = target.updateActivate(RECRUITMENT_ID);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void 멘토링모집서삭제실패_본인아님() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .user(User.builder().id(-1L).build())
                        .build()))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.deleteMentoringRecruitment(RECRUITMENT_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.FORBIDDEN);
    }
    @Test
    public void 멘토링모집서삭제실패_존재하지않음() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.empty())
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        final ApiException result = assertThrows(ApiException.class,
                () -> target.updateActivate(RECRUITMENT_ID));

        // then
        assertThat(result.getStatus()).isEqualTo(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT);
    }
    @Test
    public void 멘토링모집서삭제성공() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(
                MentoringRecruitment.builder()
                        .user(User.builder().id(USER_ID).build())
                        .build()))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        target.deleteMentoringRecruitment(RECRUITMENT_ID);

        // then
    }

    @Test
    public void 멘토링모집서좋아요성공() {
        // given
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.empty())
                .when(mentorRecommendationRepository)
                .findByUserIdAndRecruitmentId(anyLong(), anyLong());
        doReturn(1L)
                .when(mentoringRecruitmentRepository)
                .countRecommendation(anyLong());
        doReturn(Optional.of(MentoringRecruitment.builder().build()))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());
        // when
        RecommendationResponseDto result = target.mentorRecommendation(RECRUITMENT_ID);

        // then
        assertThat(result.isHasRecommend()).isTrue();
    }

    @Test
    public void 멘토링모집서좋아요취소성공() {
        doReturn(USER_ID)
                .when(userService)
                .getLoginMemberId();
        doReturn(Optional.of(MentorRecommendation.builder().build()))
                .when(mentorRecommendationRepository)
                .findByUserIdAndRecruitmentId(anyLong(), anyLong());
        doReturn(1L)
                .when(mentoringRecruitmentRepository)
                .countRecommendation(anyLong());
        doReturn(Optional.of(MentoringRecruitment.builder().build()))
                .when(mentoringRecruitmentRepository)
                .findById(anyLong());

        // when
        RecommendationResponseDto result = target.mentorRecommendation(RECRUITMENT_ID);

        // then
        assertThat(result.isHasRecommend()).isFalse();
    }

    private MentoringRecruitmentRequestDto updateDto() {
        return MentoringRecruitmentRequestDto.builder()
                .category("주거,일자리,생활")
                .name("멘토2")
                .brief("도와드리겠습니다.")
                .build();
    }
    private MentoringRecruitmentRequestDto createDto() {
        return MentoringRecruitmentRequestDto.builder()
                .category("주거,일자리")
                .name("멘토1")
                .brief("도와드릴게요.")
                .build();
    }
}
