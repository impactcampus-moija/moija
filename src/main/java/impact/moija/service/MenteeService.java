package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.Mentoring;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import impact.moija.dto.mentoring.MenteeRequestDto;
import impact.moija.repository.mentoring.MenteeRepository;
import impact.moija.repository.mentoring.MentorRepository;
import impact.moija.repository.mentoring.MentoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void applyMentee(Long mentorId, MenteeRequestDto dto) {
        Mentee mentee = menteeRepository.save(dto.toEntity(
                User.builder()
                        .id(userService.getLoginMemberId())
                        .build()
                , MentoringStatus.PENDING
        ));

        Mentor mentor = findMentor(mentorId);

        mentoringRepository.save(
                Mentoring.builder()
                        .mentee(mentee)
                        .mentor(mentor)
                        .build()
        );
    }
}
