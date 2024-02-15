package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationDetailResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationListResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationRequestDto;
import impact.moija.dto.mentoring.MentoringApplicationHandlingRequestDto;
import impact.moija.service.MentoringApplicationService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mentoring")
public class MentoringApplicationController {

    private final MentoringApplicationService applicationService;

    @PostMapping("/recruitments/{recruitmentId}/application")
    @PreAuthorize("hasAnyRole('ROLE_INDEPENDENCE')")
    public BaseResponse<PkResponseDto> createMentoringApplication(
            @Valid @RequestBody MentoringApplicationRequestDto application,
            @PathVariable Long recruitmentId) {
        PkResponseDto id = applicationService.createMentoringApplication(recruitmentId, application);
        return BaseResponse.created(id);
    }

    @GetMapping("/recruitments/{recruitmentId}/applications")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public BaseResponse<List<MentoringApplicationListResponseDto>> getPendingMentoringApplications(
            @PathVariable Long recruitmentId
    ) {
        List<MentoringApplicationListResponseDto> applications = applicationService
                .getPendingMentoringApplications(recruitmentId);

        return BaseResponse.ok(applications);
    }

    @GetMapping("/recruitments/{recruitmentId}/applications/{applicationId}")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR', 'ROLE_INDEPENDENCE')")
    public BaseResponse<MentoringApplicationDetailResponseDto> getMentoringApplication(
            @PathVariable Long recruitmentId,
            @PathVariable Long applicationId
    ) {
        MentoringApplicationDetailResponseDto application = applicationService
                .getMentoringApplication(recruitmentId, applicationId);

        return BaseResponse.ok(application);
    }

    @PutMapping("/recruitments/{recruitmentId}/applications/{applicationId}")
    @PreAuthorize("hasAnyRole('ROLE_INDEPENDENCE')")
    public BaseResponse<PkResponseDto> updateMentoringApplication(
            @Valid @RequestBody MentoringApplicationRequestDto application,
            @PathVariable Long recruitmentId,
            @PathVariable Long applicationId
    ) {
        PkResponseDto id = applicationService.updateMentoringApplication(applicationId, application);
        return BaseResponse.ok(id);
    }

    @DeleteMapping("/recruitments/{recruitmentId}/applications/{applicationId}")
    @PreAuthorize("hasAnyRole('ROLE_INDEPENDENCE')")
    public BaseResponse<Void> deleteMentoringApplication(
            @PathVariable Long recruitmentId,
            @PathVariable Long applicationId
    ) {
        applicationService.deleteMentoringApplication(applicationId);
        return BaseResponse.ok();
    }

    @PatchMapping("/recruitments/{recruitmentId}/applications/{applicationId}/handling")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public BaseResponse<PkResponseDto> handleMentoringApplication(
            @RequestBody @Valid MentoringApplicationHandlingRequestDto handling,
            @PathVariable Long recruitmentId,
            @PathVariable Long applicationId
    ) {
        PkResponseDto id = applicationService.handleMentoringApplication(recruitmentId, applicationId, handling);
        return BaseResponse.ok(id);
    }
}
