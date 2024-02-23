package impact.moija.controller;

import impact.moija.api.BaseResponse;

import impact.moija.dto.mentoring.MentoringApplicationListResponseDto;
import impact.moija.dto.user.IndependenceRequestDto;
import impact.moija.service.MentoringApplicationService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my")
public class UserController {
    private final MentoringApplicationService applicationService;

    @GetMapping("/mentoring/applications")
    @PreAuthorize("hasAnyRole('ROLE_INDEPENDENCE')")
    public BaseResponse<List<MentoringApplicationListResponseDto>> getMyMentoringApplications() {
        List<MentoringApplicationListResponseDto> applications = applicationService
                .getMyMentoringApplications();

        return BaseResponse.ok(applications);
    }
}
