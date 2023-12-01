package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringResponseDto;
import impact.moija.dto.mentoring.MentoringRequestDto;
import impact.moija.service.MentoringService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('MENTOR')")
public class MentoringController {

    private final MentoringService mentoringService;

    @GetMapping("/mentoring/mentor")
    public BaseResponse<List<MentoringResponseDto>> getPendingMentoring() {
        return BaseResponse.ok(mentoringService.getPendingMentoring());
    }

    @GetMapping("/mentoring/mentee")
    @PreAuthorize("hasAnyRole('MENTOR', 'INDEPENDENCE')")
    public BaseResponse<List<MentoringResponseDto>> getMyMentoring() {
        return BaseResponse.ok(mentoringService.getMyMentoring());
    }

    @GetMapping("/mentoring/{mentoringId}")
    @PreAuthorize("hasAnyRole('MENTOR', 'INDEPENDENCE')")
    public BaseResponse<MentoringResponseDto> getMentoring(@PathVariable Long mentoringId) {
        return BaseResponse.ok(mentoringService.getMentoring(mentoringId));
    }

    @PatchMapping("/mentoring/{mentoringId}")
    public BaseResponse<PkResponseDto> updateMenteeStatus(@PathVariable Long mentoringId,
                                                          @RequestBody MentoringRequestDto mentoring) {
        PkResponseDto id = mentoringService.updateMentoringStatus(mentoringId, mentoring);
        return BaseResponse.ok(id);
    }
}
