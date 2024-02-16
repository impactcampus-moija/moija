package impact.moija.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentoringRecruitmentService {

    private final MentoringRecruitmentRepository mentoringRecruitmentRepository;
    private final MentorRecommendationRepository mentorRecommendationRepository;
    private final UserService userService;
    private final ImageService imageService;

    private MentoringRecruitment findMentoringRecruitment(Long recruitmentId) {
        return mentoringRecruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT));
    }

    @Transactional
    public PkResponseDto createMentoringRecruitment(MentoringRecruitmentRequestDto dto, MultipartFile image) {

        Long loginUserId = userService.getLoginMemberId();

        // 찾았을 때, 있으면 Throw 해야 함.
        final MentoringRecruitment findRecruitment = mentoringRecruitmentRepository.findByUserId(loginUserId)
                .orElse(null);

        if (findRecruitment != null)  {
            throw new ApiException(MoijaHttpStatus.DUPLICATE_MENTORING_RECRUITMENT);
        }

        MentoringRecruitment recruitment = mentoringRecruitmentRepository.save(dto.toEntity(loginUserId, false));

        // dirty checking
        if (image != null && !image.isEmpty()) {
            recruitment.setImageUrl(imageService.createImage("mentoringRecruitment", recruitment.getId(), image));
        }

        return PkResponseDto.of(recruitment.getId());
    }

    public PageResponse<MentoringRecruitmentListResponseDto> getMentoringRecruitments(Pageable pageable,
                                                                                      String category,
                                                                                      String keyword) {

        return PageResponse.of(
                mentoringRecruitmentRepository.findByCategoryAndKeyword(pageable, category, keyword).map(
                        MentoringRecruitmentListResponseDto::of
                )
        );
    }

    public MentoringRecruitmentDetailResponseDto getMentoringRecruitment(Long recruitmentId) {
        MentoringRecruitment recruitment = findMentoringRecruitment(recruitmentId);

        return MentoringRecruitmentDetailResponseDto.of(recruitment);
    }
    @Transactional
    public PkResponseDto updateMentoringRecruitment(MentoringRecruitmentRequestDto dto,
                                                    MultipartFile image,
                                                    Long recruitmentId) {
        Long loginUserId = userService.getLoginMemberId();
        MentoringRecruitment recruitment = findMentoringRecruitment(recruitmentId);

        if(!recruitment.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        recruitment.updateRecruitment(dto);

        if (image != null && !image.isEmpty()) {
            imageService.deleteImage("mentoringRecruitment", recruitmentId);
            recruitment.setImageUrl(imageService.createImage("mentoringRecruitment", recruitmentId, image));
        }

        return PkResponseDto.of(recruitmentId);
    }

    @Transactional
    public PkResponseDto updateActivate(Long recruitmentId) {

        Long loginUserId = userService.getLoginMemberId();
        MentoringRecruitment recruitment = findMentoringRecruitment(recruitmentId);

        if(!recruitment.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        recruitment.convertActivate();

        return PkResponseDto.of(recruitmentId);
    }

    public void deleteMentoringRecruitment(Long recruitmentId) {
        Long loginUserId = userService.getLoginMemberId();
        MentoringRecruitment recruitment = findMentoringRecruitment(recruitmentId);

        if(!recruitment.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        mentoringRecruitmentRepository.delete(recruitment);
    }

    public RecommendationResponseDto mentorRecommendation(Long recruitmentId) {
        Long loginUserId = userService.getLoginMemberId();
        MentoringRecruitment recruitment = findMentoringRecruitment(recruitmentId);

        MentorRecommendation recommendation = mentorRecommendationRepository
                .findByUserIdAndRecruitmentId(recruitmentId, loginUserId).orElse(null);

        if (recommendation == null) {
            return createMentorRecommendation(recruitmentId, loginUserId);
        }

        return deleteMentorRecommendation(recruitmentId, recommendation);
    }

    private RecommendationResponseDto createMentorRecommendation(Long recruitmentId, Long userId) {
        MentorRecommendation recommendation = MentorRecommendation.builder()
                .recruitment(MentoringRecruitment.builder().id(recruitmentId).build())
                .user(User.builder().id(userId).build())
                .build();

        mentorRecommendationRepository.save(recommendation);

        return RecommendationResponseDto.builder()
                .hasRecommend(true)
                .recommendationCount(mentoringRecruitmentRepository.countRecommendation(recruitmentId))
                .build();
    }

    private RecommendationResponseDto deleteMentorRecommendation(Long recruitmentId, MentorRecommendation recommendation) {
        mentorRecommendationRepository.delete(recommendation);

        return RecommendationResponseDto.builder()
                .hasRecommend(false)
                .recommendationCount(mentoringRecruitmentRepository.countRecommendation(recruitmentId))
                .build();
    }
}
