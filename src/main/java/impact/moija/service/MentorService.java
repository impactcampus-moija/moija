package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.Mentoring;
import impact.moija.domain.mentoring.MentoringTag;
import impact.moija.domain.user.User;
import impact.moija.dto.mentoring.MentorListResponseDto;
import impact.moija.dto.mentoring.MentorRequestDto;
import impact.moija.repository.mentoring.MentorRepository;
import impact.moija.repository.mentoring.MentoringRepository;
import impact.moija.repository.mentoring.MentoringTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class MentorService {

    private final MentorRepository mentorRepository;
    private final MentoringRepository mentoringRepository;
    private final MentoringTagRepository tagRepository;
    private final UserService userService;
    private final ImageService imageService;

    private MentoringTag findTag(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_TAG));
    }

    public void applyMentor(MentorRequestDto dto, MultipartFile file) {

        Mentor mentor = mentorRepository.save(dto.toEntity(
                User.builder()
                    .id(userService.getLoginMemberId())
                    .build()
        ));

        if (file != null && !file.isEmpty()) {
            imageService.createImage("mentor", mentor.getId(), file);
        }

        for(String tagName : dto.getTags()) {
            MentoringTag tag = findTag(tagName);

            mentoringRepository.save(
                    Mentoring.builder()
                        .mentor(mentor)
                        .tag(tag)
                        .activation(true) // TODO : 수정 가능성 있음
                        .build()
            );
        }
    }

}
