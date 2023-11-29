package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.mentoring.MenteeDetailResponseDto;
import impact.moija.dto.mentoring.MenteeRequestDto;
import impact.moija.service.MenteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MenteeController {

    private final MenteeService menteeService;

    @PostMapping("/mentees/mentor/{mentorId}")
    public BaseResponse<Void> applyMentee(@PathVariable Long mentorId,
                                          @RequestBody MenteeRequestDto mentee) {
        menteeService.applyMentee(mentorId, mentee);
        return BaseResponse.ok();
    }

    @GetMapping("/mentees/{menteeId}")
    public BaseResponse<MenteeDetailResponseDto> getMentee(@PathVariable Long menteeId) {
        return BaseResponse.ok(menteeService.getMentee(menteeId));
    }

    @PutMapping("/mentees/{menteeId}")
    public BaseResponse<Void> updateMentee(@PathVariable Long menteeId,
                                       @RequestBody MenteeRequestDto mentee) {
        menteeService.updateMentee(menteeId, mentee);
        return BaseResponse.ok();
    }

    @DeleteMapping("/mentees/{menteeId}")
    public BaseResponse<Void> deleteMentee(@PathVariable Long menteeId) {
        menteeService.deleteMentee(menteeId);
        return BaseResponse.ok();
    }
}
