package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.domain.mentoring.MentoringTag;
import impact.moija.domain.user.User;
import impact.moija.dto.common.ImageResponseDto;
import impact.moija.dto.mentoring.MentorListResponseDto;
import impact.moija.dto.mentoring.MentorRequestDto;
import impact.moija.repository.mentoring.MentorRepository;
import impact.moija.repository.mentoring.MentoringRecruitmentRepository;
import impact.moija.repository.mentoring.MentoringTagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class MentorService {

    private final MentorRepository mentorRepository;
    private final MentoringRecruitmentRepository recruitmentRepository;
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

            recruitmentRepository.save(
                    MentoringRecruitment.builder()
                        .mentor(mentor)
                        .tag(tag)
                        .build()
            );
        }
    }

    public Page<MentorListResponseDto> getMentors(String tagName, Pageable pageable) {
        List<Mentor> mentors;
        if (tagName != null) {
            mentors = mentorRepository.findByTagAndActivateIsTrue(findTag(tagName));
        } else {
            mentors = mentorRepository.findByActivateIsTrue();
        }

        List<MentorListResponseDto> dtos = mentors.stream().map(mentor -> {
            ImageResponseDto image = imageService.getImage("mentor", mentor.getId());
            return MentorListResponseDto.of(mentor, image.getUrl());
        }).toList();

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

    public Page<MentorListResponseDto> getSearchMentors(String keyword, Pageable pageable) {
        Page<Mentor> mentors = mentorRepository.findByBriefContainingAndActivateIsTrue(keyword, pageable);

        return mentors.map(mentor -> {
            ImageResponseDto image = imageService.getImage("mentor", mentor.getId());
            return MentorListResponseDto.of(mentor, image.getUrl());
        });
    }
}
