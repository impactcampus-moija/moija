package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.mentoring.InactiveIndependenceResponseDto;
import impact.moija.dto.mentoring.InactiveMentorResponseDto;
import impact.moija.service.AdminService;
import impact.moija.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PolicyService policyService;
    private final AdminService adminService;

    @PostMapping("/policies")
    public BaseResponse<Void> createPolicies(@RequestParam String display,
                                             @RequestParam String page) {
        policyService.createPolicies(display, page);
        return BaseResponse.ok();
    }

    @GetMapping("/inactive-mentors")
    public BaseResponse<PageResponse<InactiveMentorResponseDto>> getInactiveMentors(final Pageable pageable) {
        return BaseResponse.ok(adminService.getInactivateMentors(pageable));
    }

    @PutMapping("/mentor/activate/{userId}")
    public BaseResponse<Void> activateMentor(@PathVariable final Long userId) {
        adminService.activateMentor(userId);
        return BaseResponse.ok();
    }

    @GetMapping("/inactive-independence")
    public BaseResponse<PageResponse<InactiveIndependenceResponseDto>> getInactivateIndependence(final Pageable pageable) {
        return BaseResponse.ok(adminService.getInactivateIndependence(pageable));
    }

    @PutMapping("/independence/activate/{userId}")
    public BaseResponse<Void> activateIndependence(@PathVariable final Long userId) {
        adminService.activateIndependence(userId);
        return BaseResponse.ok();
    }
}
