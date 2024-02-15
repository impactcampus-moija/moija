package impact.moija.service;

import com.amazonaws.util.StringUtils;
import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.MentoringApplication;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationDetailResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationListResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationRequestDto;
import impact.moija.dto.mentoring.MentoringApplicationHandlingRequestDto;
import impact.moija.repository.mentoring.MentoringApplicationRepository;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentoringApplicationService {
    private final UserService userService;
    private final MentoringApplicationRepository applicationRepository;
    private final MentoringRecruitmentRepository recruitmentRepository;

    private MentoringRecruitment findRecruitment(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT));
    }

    private MentoringApplication findApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTORING_APPLICATION));
    }
    @Transactional
    public PkResponseDto createMentoringApplication(Long recruitmentId,
                                                    MentoringApplicationRequestDto dto) {
        Long loginUserId = userService.getLoginMemberId();

        // 같은 멘토에 대한 중복 지원 예외 처리
        MentoringApplication application = applicationRepository
                .findByUserIdAndRecruitmentId(loginUserId, recruitmentId)
                .orElse(null);

        if (application != null) {
            throw new ApiException(MoijaHttpStatus.DUPLICATE_MENTORING_APPLICATION);
        }

        // 모집서 작성자와 지원서 작성자 동일에 대한 예외 처리
        MentoringRecruitment recruitment = recruitmentRepository
                .findByUserId(loginUserId)
                .orElse(null);

        if (recruitment != null && recruitment.getId().equals(recruitmentId)) {
            throw new ApiException(MoijaHttpStatus.INVALID_MENTORING_APPLICATION);
        }

        MentoringApplication result = applicationRepository.save(
                dto.toEntity(loginUserId, recruitmentId, MentoringStatus.PENDING)
        );
        return PkResponseDto.of(result.getId());
    }

    public List<MentoringApplicationListResponseDto> getPendingMentoringApplications(Long recruitmentId) {
        Long loginUserId = userService.getLoginMemberId();

        // 모집서가 없을 경우 예외 처리
        MentoringRecruitment recruitment = findRecruitment(recruitmentId);

        // 본인 모집서가 아니라면 예외 처리
        if (!recruitment.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        List<MentoringApplication> applications = applicationRepository
                .findByRecruitmentIdAndStatusIsPending(recruitmentId);

        return applications.stream().map(MentoringApplicationListResponseDto::of).toList();
    }

    public MentoringApplicationDetailResponseDto getMentoringApplication(Long recruitmentId, Long applicationId) {
        // 모집서가 없다면 예외 처리
        MentoringRecruitment recruitment = findRecruitment(recruitmentId);
        // 지원서가 없다면 예외 처리
        MentoringApplication application = findApplication(applicationId);

        Long loginUserId = userService.getLoginMemberId();

        // 멘토와 지원자가 아니라면 예외 처리
        if (!recruitment.getUser().getId().equals(loginUserId) && !application.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        return MentoringApplicationDetailResponseDto.of(application);
    }

    public PkResponseDto updateMentoringApplication(long applicationId, MentoringApplicationRequestDto dto) {
        // 지원서가 없다면 예외 처리
        MentoringApplication application = findApplication(applicationId);
        Long loginUserId = userService.getLoginMemberId();

        // 본인 지원서가 아니라면 예외 처리
        if (!application.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        application.updateApplication(dto);

        return PkResponseDto.of(applicationId);
    }

    public void deleteMentoringApplication(Long applicationId) {
        // 지원서가 없다면 예외 처리
        MentoringApplication application = findApplication(applicationId);
        Long loginUserId = userService.getLoginMemberId();

        // 본인 지원서가 아니라면 예외 처리
        if (!application.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        applicationRepository.delete(application);
    }


    public PkResponseDto handleMentoringApplication(Long recruitmentId,
                                                    Long applicationId,
                                                    MentoringApplicationHandlingRequestDto dto) {
        // 모집서가 없다면 예외 처리
        MentoringRecruitment recruitment = findRecruitment(recruitmentId);
        // 지원서가 없다면 예외 처리
        MentoringApplication application = findApplication(applicationId);

        // 본인이 아니라면 예외 처리
        Long loginUserId = userService.getLoginMemberId();
        if (!recruitment.getUser().getId().equals(loginUserId)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        if (dto.getStatus().equals(MentoringStatus.REFUSE) && StringUtils.isNullOrEmpty(dto.getReason())) {
            throw new ApiException(MoijaHttpStatus.INVALID_MENTORING_REASON);
        }

        application.handleApplication(dto);

        return PkResponseDto.of(applicationId);
    }
}
