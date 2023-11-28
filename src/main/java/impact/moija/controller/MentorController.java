package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.mentoring.MentorListResponseDto;
import impact.moija.dto.mentoring.MentorRequestDto;
import impact.moija.service.MentorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MentorController {

    private final MentorService mentorService;

    @PostMapping("/mentors")
    public BaseResponse<Void> applyMentor(@RequestPart MentorRequestDto mentor,
                                          @RequestPart(required = false) MultipartFile image) {
        mentorService.applyMentor(mentor, image);
        return BaseResponse.ok();
    }

    @GetMapping("/mentors")
    public BaseResponse<Page<MentorListResponseDto>> getMentors(@RequestParam(required = false) String tag,
                                                                @PageableDefault(size = 12) Pageable pageable) {
        return BaseResponse.ok(mentorService.getMentors(tag, pageable));
    }


}
