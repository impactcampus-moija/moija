package impact.moija.controller;

import impact.moija.api.BaseResponse;
<<<<<<< HEAD
import impact.moija.dto.mentoring.MentoringApplicationListResponseDto;
import impact.moija.service.MentoringApplicationService;
=======
import impact.moija.domain.user.Location;
import impact.moija.domain.user.User;
import impact.moija.dto.jwt.TokenResponseDto;
import impact.moija.dto.user.AuthRequestDto;
import impact.moija.dto.user.IndependenceRequestDto;
import impact.moija.dto.user.SignupRequestDto;
>>>>>>> bd08c716e015aa949f7da78c484bd954645d0b04
import impact.moija.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
>>>>>>> bd08c716e015aa949f7da78c484bd954645d0b04
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my")
public class UserController {
    private final MentoringApplicationService applicationService;

    @GetMapping("/mentoring/applications")
    @PreAuthorize("hasAnyRole('ROLE_INDEPENDENCE')")
    public BaseResponse<List<MentoringApplicationListResponseDto>> getMyMentoringApplications() {
        List<MentoringApplicationListResponseDto> applications = applicationService
                .getMyMentoringApplications();

<<<<<<< HEAD
        return BaseResponse.ok(applications);
=======
    @PostMapping("/users/independence-certificate")
    public BaseResponse<Void> certificateIndependence(@RequestBody IndependenceRequestDto independenceRequestDto) {
        userService.certificateIndependence(independenceRequestDto);
        return BaseResponse.ok();
>>>>>>> bd08c716e015aa949f7da78c484bd954645d0b04
    }
}
