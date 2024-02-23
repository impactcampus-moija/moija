package impact.moija.controller;

import impact.moija.dto.common.PageResponse;
import impact.moija.dto.policy.PolicyResponse;
import impact.moija.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class PolicyController {
    private final PolicyService policyService;

    @GetMapping("/policies/search")
    public PageResponse<PolicyResponse> searchPolicies(
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) String searchText,
            Pageable pageable
    ) {
        return policyService.searchPolicies(age, locationName, searchText, pageable);
    }
}
