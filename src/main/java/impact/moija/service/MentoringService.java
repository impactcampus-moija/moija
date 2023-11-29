package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.dto.mentoring.MenteeListResponseDto;
import impact.moija.repository.mentoring.MenteeRepository;
import impact.moija.repository.mentoring.MentorRepository;
import impact.moija.repository.mentoring.MentoringRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MentoringService {

    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    private final UserService userService;

    private Mentor findMentor(Long mentorId) {
        return mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTOR));
    }

    @Transactional
    public List<MenteeListResponseDto> getPendingMentoring(Long mentorId) {
        Mentor mentor = findMentor(mentorId);
        List<Mentee> mentees = menteeRepository.findByMentorWithMentoringStatus(mentor, MentoringStatus.PENDING);
        return mentees.stream()
                .map(mentee -> MenteeListResponseDto.of(mentee, mentee.getUser()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MenteeListResponseDto> getMyMentoring() {
        List<Mentee> mentees = menteeRepository.findByMenteeWithoutMentoringStatus(
                userService.getLoginMemberId()
                , MentoringStatus.PENDING
        );

        return mentees.stream()
                .map(mentee -> MenteeListResponseDto.of(mentee, mentee.getUser()))
                .collect(Collectors.toList());
    }
}
