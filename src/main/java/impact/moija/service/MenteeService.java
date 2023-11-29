package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.Mentoring;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MenteeDetailResponseDto;
import impact.moija.dto.mentoring.MenteeRequestDto;
import impact.moija.repository.mentoring.MenteeRepository;
import impact.moija.repository.mentoring.MentorRepository;
import impact.moija.repository.mentoring.MentoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MenteeService {

    private final MenteeRepository menteeRepository;
    private final MentorRepository mentorRepository;
    private final MentoringRepository mentoringRepository;
    private final UserService userService;

    private Mentor findMentor(Long mentorId) {
        return mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTOR));
    }

    private Mentee findMentee(Long menteeId) {
        return menteeRepository.findById(menteeId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTEE));
    }

    @Transactional
    public PkResponseDto applyMentee(Long mentorId, MenteeRequestDto dto) {
        Mentor mentor = findMentor(mentorId);

        if (mentor.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        Mentee mentee = menteeRepository.save(dto.toEntity(
                User.builder()
                        .id(userService.getLoginMemberId())
                        .build()
                , MentoringStatus.PENDING
        ));

        mentoringRepository.save(
                Mentoring.builder()
                        .mentee(mentee)
                        .mentor(mentor)
                        .build()
        );

        return PkResponseDto.of(mentee.getId());
    }

    @Transactional
    public MenteeDetailResponseDto getMentee(Long menteeId) {
        Mentee mentee = findMentee(menteeId);
        return MenteeDetailResponseDto.of(mentee, mentee.getUser());
    }

    @Transactional
    public PkResponseDto updateMentee(Long menteeId, MenteeRequestDto mentee) {
        Mentee oldMentee = findMentee(menteeId);

        if(!oldMentee.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        oldMentee.updateMentee(mentee);
        menteeRepository.save(oldMentee);

        return PkResponseDto.of(oldMentee.getId());
    }

    @Transactional
    public void deleteMentee(Long menteeId) {
        Mentee mentee = findMentee(menteeId);

        if(!mentee.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        mentoringRepository.deleteAllByMentee(mentee);
        menteeRepository.delete(mentee);
    }
}
