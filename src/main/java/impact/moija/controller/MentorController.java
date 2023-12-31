package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.common.RecommendationResponseDto;
import impact.moija.dto.mentoring.MentorDetailResponseDto;
import impact.moija.dto.mentoring.MentorListResponseDto;
import impact.moija.dto.mentoring.MentorRequestDto;
import impact.moija.dto.mentoring.MentoringReviewRequestDto;
import impact.moija.dto.mentoring.MentoringReviewResponseDto;
import impact.moija.service.MentorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('USER')")
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
    @PreAuthorize("hasAnyRole('MENTOR')")
    public BaseResponse<PkResponseDto> updateMentor(@RequestPart MentorRequestDto mentor,
                                                    @RequestPart(required = false) MultipartFile image,
                                                    @PathVariable Long mentorId) {
        PkResponseDto id = mentorService.updateMentor(mentor, image, mentorId);
        return BaseResponse.ok(id);
    }

    @DeleteMapping("/mentors/{mentorId}")
    @PreAuthorize("hasAnyRole('MENTOR')")
    public BaseResponse<Void> deleteMentor(@PathVariable Long mentorId) {
        mentorService.deleteMentor(mentorId);
        return BaseResponse.ok();
    }

    @GetMapping("/mentors/me")
    public BaseResponse<List<MentorListResponseDto>> getMyMentors() {
        return BaseResponse.ok(mentorService.getMyMentors());
    }

    @PatchMapping("/mentors/{mentorId}/activate")
    @PreAuthorize("hasAnyRole('MENTOR')")
    public BaseResponse<PkResponseDto> activateMentor(@PathVariable Long mentorId) {
        PkResponseDto id = mentorService.activateMentor(mentorId);
        return BaseResponse.ok(id);
    }

    @PatchMapping("/mentors/{mentorId}/deactivate")
    @PreAuthorize("hasAnyRole('MENTOR')")
    public BaseResponse<PkResponseDto> deactivateMentor(@PathVariable Long mentorId) {
        PkResponseDto id = mentorService.deactivateMentor(mentorId);
        return BaseResponse.ok(id);
    }

    // TODO : 파일 분리
    @PostMapping("/mentors/{mentorId}/reviews")
    @PreAuthorize("hasAnyRole('INDEPENDENCE')")
    public BaseResponse<PkResponseDto> createMentorReview(@PathVariable Long mentorId,
                                                          @RequestBody MentoringReviewRequestDto review) {
        PkResponseDto id = mentorService.createMentorReview(mentorId, review);
        return BaseResponse.created(id);
    }

    @GetMapping("/mentors/{mentorId}/reviews")
    public BaseResponse<List<MentoringReviewResponseDto>> getMentorReviews(@PathVariable Long mentorId) {
        return BaseResponse.ok(mentorService.getMentorReviews(mentorId));
    }

    @GetMapping("/mentors/reviews/{reviewId}")
    public BaseResponse<MentoringReviewResponseDto> getMentorReview(@PathVariable Long reviewId) {
        return BaseResponse.ok(mentorService.getMentorReview(reviewId));
    }

    @PostMapping("/mentors/{mentorId}/recommendations")
    public BaseResponse<RecommendationResponseDto> likeMentor(@PathVariable Long mentorId) {
        return BaseResponse.ok(mentorService.likeMentor(mentorId));
    }
}
