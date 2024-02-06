package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentDetailResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentListResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentRequestDto;
import impact.moija.service.MentoringRecruitmentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mentoring")
public class MentoringRecruitmentController {

    private final MentoringRecruitmentService recruitmentService;

    @PostMapping("/recruitment")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public BaseResponse<PkResponseDto> createRecruitment(@Valid @RequestPart MentoringRecruitmentRequestDto recruitment,
                                                         @RequestPart(required = false) MultipartFile image) {
        PkResponseDto id = recruitmentService.createMentoringRecruitment(recruitment, image);
        return BaseResponse.created(id);
    }

    @GetMapping("/recruitments")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public BaseResponse<PageResponse<MentoringRecruitmentListResponseDto>> getAllMentoringRecruitments(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        return BaseResponse.ok(recruitmentService.getMentoringRecruitments(pageable, category, keyword));
    }

    @GetMapping("/recruitments/{recruitmentId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public BaseResponse<MentoringRecruitmentDetailResponseDto> getMentoringRecruitment(
            @PathVariable Long recruitmentId
    ) {
        return BaseResponse.ok(recruitmentService.getMentoringRecruitment(recruitmentId));
    }

    @PatchMapping("/recruitments/{recruitmentId}")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public BaseResponse<PkResponseDto> updateMentoringRecruitment(
           @Valid @RequestPart MentoringRecruitmentRequestDto recruitment,
           @RequestPart MultipartFile image,
           @PathVariable Long recruitmentId
    ) {
        return BaseResponse.ok(recruitmentService.updateMentoringRecruitment(recruitment, image, recruitmentId));
    }

    @PatchMapping("/recruitments/{recruitmentId}/activate")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public BaseResponse<PkResponseDto> updateActivate(@PathVariable Long recruitmentId) {
        return BaseResponse.ok(recruitmentService.updateActivate(recruitmentId));
    }

    @DeleteMapping("/recruitments/{recruitmentId}")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public BaseResponse<Void> deleteMentoringRecruitment(@PathVariable Long recruitmentId) {
        recruitmentService.deleteMentoringRecruitment(recruitmentId);
        return BaseResponse.ok();
    }

}
