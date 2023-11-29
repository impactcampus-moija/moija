package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.mentoring.MenteeListResponseDto;
import impact.moija.service.MentoringService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MentoringController {

    private final MentoringService mentoringService;

    @GetMapping("/mentoring/{mentorId}/pending")
    public BaseResponse<List<MenteeListResponseDto>> getPendingMentoring(@PathVariable Long mentorId) {
        return BaseResponse.ok(mentoringService.getPendingMentoring(mentorId));
    }

    @GetMapping("/my/mentoring")
    public BaseResponse<List<MenteeListResponseDto>> getMyMentoring() {
        return BaseResponse.ok(mentoringService.getMyMentoring());
    }

}
