package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.Mentoring;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringResponseDto;
import impact.moija.dto.mentoring.MentoringRequestDto;
import impact.moija.repository.mentoring.MentoringRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MentoringService {
    private final MentoringRepository mentoringRepository;
    private final UserService userService;

    private Mentoring findMentoring(Long mentoringId) {
        return mentoringRepository.findById(mentoringId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTORING));
    }


    @Transactional
    public List<MentoringResponseDto> getPendingMentoring() {
        List<Mentoring> mentorings = mentoringRepository.findByUserIdAndStatus(
                userService.getLoginMemberId()
                , MentoringStatus.PENDING);

        return mentorings.stream()
                .map(MentoringResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MentoringResponseDto> getMyMentoring() {
        List<Mentoring> mentorings = mentoringRepository.findByUserIdExceptStatus(
                userService.getLoginMemberId()
                , MentoringStatus.PENDING
        );

        return mentorings.stream()
                .map(MentoringResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public MentoringResponseDto getMentoring(Long mentoringId) {
        return MentoringResponseDto.of(findMentoring(mentoringId));
    }

    @Transactional
    public PkResponseDto updateMentoringStatus(Long menteeId, MentoringRequestDto dto) {
        Mentoring mentoring = findMentoring(menteeId);
        Mentor mentor = mentoring.getMentor();

        if (!mentor.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        mentoring.updateStatus(dto);
        mentoringRepository.save(mentoring);

        return PkResponseDto.of(mentoring.getId());
    }

}
