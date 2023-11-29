package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentorDetailResponseDto;
import impact.moija.dto.mentoring.MentorListResponseDto;
import impact.moija.dto.mentoring.MentorRequestDto;
import impact.moija.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MentorController {

    private final MentorService mentorService;

    @PostMapping("/mentors")
    public BaseResponse<PkResponseDto> applyMentor(@RequestPart MentorRequestDto mentor,
                                                   @RequestPart(required = false) MultipartFile image) {
        PkResponseDto mentorId = mentorService.applyMentor(mentor, image);
        return BaseResponse.created(mentorId);
    }

    @GetMapping("/mentors")
    public BaseResponse<PageResponse<MentorListResponseDto>> getMentors(@RequestParam(required = false) String tag,
                                                                        @PageableDefault(size = 12) Pageable pageable) {
        return BaseResponse.ok(mentorService.getMentors(tag, pageable));
    }

    @GetMapping("/mentors/search")
    public BaseResponse<PageResponse<MentorListResponseDto>> getSearchMentors(@RequestParam String keyword,
                                                                              @PageableDefault(size = 12) Pageable pageable) {
        return BaseResponse.ok(mentorService.getSearchMentors(keyword, pageable));
    }

    @GetMapping("/mentors/{mentorId}")
    public BaseResponse<MentorDetailResponseDto> getMentor(@PathVariable Long mentorId) {
        return BaseResponse.ok(mentorService.getMentor(mentorId));
    }

    @PutMapping("/mentors/{mentorId}")
    public BaseResponse<PkResponseDto> updateMentor(@RequestPart MentorRequestDto mentor,
                                                    @RequestPart(required = false) MultipartFile image,
                                                    @PathVariable Long mentorId) {
        PkResponseDto id = mentorService.updateMentor(mentor, image, mentorId);
        return BaseResponse.ok(id);
    }

    @DeleteMapping("/mentors/{mentorId}")
    public BaseResponse<Void> deleteMentor(@PathVariable Long mentorId) {
        mentorService.deleteMentor(mentorId);
        return BaseResponse.ok();

    }

    @PutMapping("/mentors/{mentorId}/activate")
    public BaseResponse<PkResponseDto> activateMentor(@PathVariable Long mentorId) {
        PkResponseDto id = mentorService.activateMentor(mentorId);
        return BaseResponse.ok(id);
    }

    @PutMapping("/mentors/{mentorId}/deactivate")
    public BaseResponse<PkResponseDto> deactivateMentor(@PathVariable Long mentorId) {
        PkResponseDto id = mentorService.deactivateMentor(mentorId);
        return BaseResponse.ok(id);
    }
}
