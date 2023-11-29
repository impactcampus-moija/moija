package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.mentoring.MenteeRequestDto;
import impact.moija.service.MenteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MenteeController {

    private final MenteeService menteeService;

    @PostMapping("/mentees/{mentorId}")
    public BaseResponse<Void> applyMentee(@PathVariable Long mentorId,
                                          @RequestBody MenteeRequestDto mentee) {
        menteeService.applyMentee(mentorId, mentee);
        return BaseResponse.ok();
    }
}
