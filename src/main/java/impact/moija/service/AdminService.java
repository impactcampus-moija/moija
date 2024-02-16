package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.user.Independence;
import impact.moija.domain.user.User;
import impact.moija.domain.user.UserRole;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.mentoring.InactiveIndependenceResponseDto;
import impact.moija.dto.mentoring.InactiveMentorResponseDto;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import impact.moija.repository.user.IndependenceRepository;
import impact.moija.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final IndependenceRepository independenceRepository;
    private final MentoringRecruitmentRepository recruitmentRepository;

    @Transactional(readOnly = true)
    public PageResponse<InactiveMentorResponseDto> getInactivateMentors(final Pageable pageable) {
        Page<MentoringRecruitment> recruitmentPage = recruitmentRepository.findByActivateIsFalse(pageable);
        return PageResponse.of(recruitmentPage.map(InactiveMentorResponseDto::of));
    }

    @Transactional
    public void activateMentor(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new ApiException(MoijaHttpStatus.NOT_FOUND_USER)
        );
        user.addRole(UserRole.ROLE_MENTOR);
        user.getRecruitment().updateActivate(true);
    }

    @Transactional(readOnly = true)
    public PageResponse<InactiveIndependenceResponseDto> getInactivateIndependence(final Pageable pageable) {
        Page<Independence> independencePage = independenceRepository.findByActivateIsFalse(pageable);
        return PageResponse.of(independencePage.map(InactiveIndependenceResponseDto::of));
    }

    @Transactional
    public void activateIndependence(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new ApiException(MoijaHttpStatus.NOT_FOUND_USER)
        );
        user.addRole(UserRole.ROLE_INDEPENDENCE);
        user.getIndependence().setActivate(true);
    }
}
