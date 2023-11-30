package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.mentoring.Mentor;
import impact.moija.domain.mentoring.MentorRecruitment;
import impact.moija.domain.mentoring.MentorTag;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.domain.user.User;
import impact.moija.dto.common.ImageResponseDto;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentorDetailResponseDto;
import impact.moija.dto.mentoring.MentorListResponseDto;
import impact.moija.dto.mentoring.MentorRequestDto;
import impact.moija.repository.mentoring.MentorRecruitmentRepository;
import impact.moija.repository.mentoring.MentorRepository;
import impact.moija.repository.mentoring.MentorTagRepository;
import impact.moija.repository.mentoring.MentoringRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class MentorService {

    private final MentorRepository mentorRepository;
    private final MentorRecruitmentRepository recruitmentRepository;
    private final MentorTagRepository tagRepository;
    private final MentoringRepository mentoringRepository;
    private final UserService userService;
    private final ImageService imageService;

    private MentorTag findTag(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_TAG));
    }

    private Mentor findMentor(Long mentorId) {
        return mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_MENTOR));
    }

    @Transactional
    public PkResponseDto applyMentor(MentorRequestDto dto, MultipartFile file) {

        if (dto.getTags().isEmpty()) {
            throw new ApiException(MoijaHttpStatus.INVALID_MENTOR_TAG);
        }

        // 활성화 상태로 등록
        Mentor mentor = dto.toEntity(
                User.builder()
                        .id(userService.getLoginMemberId())
                        .build()
                , true
        );

        if (file != null && !file.isEmpty()) {
            mentor.updateImageUrl(imageService.createImage("mentor", mentor.getId(), file));
        }

        mentorRepository.save(mentor);

        for (String tagName : dto.getTags()) {
            MentorTag tag = findTag(tagName);

            recruitmentRepository.save(
                    MentorRecruitment.builder()
                            .mentor(mentor)
                            .tag(tag)
                            .build()
            );
        }

        return PkResponseDto.of(mentor.getId());
    }

    @Transactional
    public PageResponse<MentorListResponseDto> getMentors(String tagName, Pageable pageable) {
        MentorTag tag = tagRepository.findByName(tagName).orElse(null);

        List<MentorListResponseDto> mentors = mentorRepository.findByTagAndActivateIsTrue(tag)
                .stream()
                .map(mentor -> MentorListResponseDto.of(mentor, countMatchingMentor(mentor)))
                .toList();

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mentors.size());
        return PageResponse.of(
                new PageImpl<>(mentors.subList(start, end), pageable, mentors.size())
        );
    }

    @Transactional
    public PageResponse<MentorListResponseDto> getSearchMentors(String keyword, Pageable pageable) {
        Page<Mentor> mentors = mentorRepository.findByBriefContainingAndActivateIsTrue(keyword, pageable);

        return PageResponse.of(
                mentors.map(mentor ->
                        MentorListResponseDto.of(mentor, countMatchingMentor(mentor))
                )
        );
    }

    @Transactional
    public MentorDetailResponseDto getMentor(Long mentorId) {
        Mentor mentor = findMentor(mentorId);
        ImageResponseDto image = imageService.getImage("mentor", mentor.getId());
        return MentorDetailResponseDto.of(
                mentor,
                image.getUrl(),
                countMatchingMentor(mentor)
        );
    }

    @Transactional
    public List<MentorListResponseDto> getMyMentors() {
        List<Mentor> mentors = mentorRepository.findByUserId(userService.getLoginMemberId());

        return mentors.stream()
                .map(mentor -> MentorListResponseDto.of(mentor, countMatchingMentor(mentor)))
                .collect(Collectors.toList());
    }

    @Transactional
    public PkResponseDto updateMentor(MentorRequestDto mentor, MultipartFile file, Long mentorId) {
        Mentor oldMentor = findMentor(mentorId);

        if (!oldMentor.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        if (file != null) {
            imageService.deleteImage("mentor", oldMentor.getId());
            oldMentor.updateImageUrl(imageService.createImage("mentor", oldMentor.getId(), file));
        }

        if (!mentor.getTags().isEmpty()) {
            recruitmentRepository.deleteAllByMentor(oldMentor);

            for (String tagName : mentor.getTags()) {
                recruitmentRepository.save(
                        MentorRecruitment.builder()
                                .mentor(oldMentor)
                                .tag(findTag(tagName))
                                .build()
                );
            }
        }

        oldMentor.updateMentor(mentor);
        Mentor newMentor = mentorRepository.save(oldMentor);

        return PkResponseDto.of(newMentor.getId());
    }

    @Transactional
    public void deleteMentor(Long mentorId) {
        Mentor mentor = findMentor(mentorId);

        if (!mentor.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        imageService.deleteImage("mentor", mentor.getId());
        recruitmentRepository.deleteAllByMentor(mentor);
        mentorRepository.delete(mentor);
    }

    @Transactional
    public PkResponseDto activateMentor(Long mentorId) {
        Mentor mentor = findMentor(mentorId);

        if (!mentor.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        if (mentor.isActivate()) {
            throw new ApiException(MoijaHttpStatus.BAD_REQUEST);
        }

        mentor.updateActivate(true);
        mentorRepository.save(mentor);

        return PkResponseDto.of(mentor.getId());
    }

    @Transactional
    public PkResponseDto deactivateMentor(Long mentorId) {
        Mentor mentor = findMentor(mentorId);

        if (!mentor.getUser().getId().equals(userService.getLoginMemberId())) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        if (!mentor.isActivate()) {
            throw new ApiException(MoijaHttpStatus.BAD_REQUEST);
        }

        mentor.updateActivate(false);
        mentorRepository.save(mentor);

        return PkResponseDto.of(mentor.getId());
    }

    private long countMatchingMentor(Mentor mentor) {
        return mentoringRepository.countMatchingMentor(
                mentor,
                MentoringStatus.PROGRESS,
                MentoringStatus.CLOSE
        );
    }
}
