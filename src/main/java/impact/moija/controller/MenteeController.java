package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MenteeResponseDto;
import impact.moija.dto.mentoring.MenteeRequestDto;
import impact.moija.service.MenteeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/mentors/{mentorId}/mentees")
    @PreAuthorize("hasAnyRole('INDEPENDENCE')")
    public BaseResponse<PkResponseDto> applyMentee(@PathVariable Long mentorId,
                                                   @RequestBody MenteeRequestDto mentee) {
        PkResponseDto id = menteeService.applyMentee(mentorId, mentee);
        return BaseResponse.created(id);
    }

    @GetMapping("/mentees/{menteeId}")
    @PreAuthorize("hasAnyRole('MENTOR', 'INDEPENDENCE')")
    public BaseResponse<MenteeResponseDto> getMentee(@PathVariable Long menteeId) {
        return BaseResponse.ok(menteeService.getMentee(menteeId));
    }

    @GetMapping("/mentees/me")
    @PreAuthorize("hasAnyRole('MENTOR')")
    public BaseResponse<List<MenteeResponseDto>> getMyMentees() {
        return BaseResponse.ok(menteeService.getMyMentees());
    }

    @PutMapping("/mentees/{menteeId}")
    @PreAuthorize("hasAnyRole('INDEPENDENCE')")
    public BaseResponse<PkResponseDto> updateMentee(@PathVariable Long menteeId,
                                                    @RequestBody MenteeRequestDto mentee) {
        PkResponseDto id = menteeService.updateMentee(menteeId, mentee);
        return BaseResponse.ok(id);
    }

    @DeleteMapping("/mentees/{menteeId}")
    @PreAuthorize("hasAnyRole('INDEPENDENCE')")
    public BaseResponse<Void> deleteMentee(@PathVariable Long menteeId) {
        menteeService.deleteMentee(menteeId);
        return BaseResponse.ok();
    }
}
