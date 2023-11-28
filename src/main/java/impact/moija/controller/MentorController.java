package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.dto.mentoring.MentorRequestDto;
import impact.moija.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
                                          @RequestPart MultipartFile image) {
        mentorService.applyMentor(mentor, image);
        return BaseResponse.ok();
    }
}
