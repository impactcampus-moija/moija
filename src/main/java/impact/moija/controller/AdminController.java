package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PolicyService policyService;

    @PostMapping("/policies")
    public BaseResponse<Void> createPolicies(@RequestParam String display,
                                             @RequestParam String page) {
        policyService.createPolicies(display, page);
        return BaseResponse.ok();
    }
}
