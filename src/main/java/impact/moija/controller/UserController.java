package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.domain.user.User;
import impact.moija.dto.jwt.TokenResponseDto;
import impact.moija.dto.user.AuthRequestDto;
import impact.moija.dto.user.SignupRequestDto;
import impact.moija.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public User signup(@RequestBody SignupRequestDto signupRequestDto){
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/user/login")
    public BaseResponse<TokenResponseDto> login(HttpServletResponse response, @RequestBody AuthRequestDto authRequestDto) {
        return BaseResponse.ok(userService.login(response, authRequestDto));
    }

    @PostMapping("/user/refresh")
    public BaseResponse<TokenResponseDto> refreshAccessToken(@CookieValue("Refresh-Token") String refreshToken) {
        return BaseResponse.ok(userService.refreshAccessToken(refreshToken));
    }

    @PostMapping("/user/logout")
    public BaseResponse<Void> logout(@CookieValue("Refresh-Token") String refreshToken) {
        userService.logout(refreshToken);
        return BaseResponse.ok();
    }
}
