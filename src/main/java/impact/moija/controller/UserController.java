package impact.moija.controller;

import impact.moija.api.BaseResponse;
import impact.moija.domain.user.Location;
import impact.moija.domain.user.User;
import impact.moija.dto.jwt.TokenResponseDto;
import impact.moija.dto.user.AuthRequestDto;
import impact.moija.dto.user.SignupRequestDto;
import impact.moija.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/locations")
    public BaseResponse<Set<String>> getLocationNames() {
        return BaseResponse.ok(Location.getNames());
    }

    @PostMapping("/users/signup")
    public User signup(@RequestBody SignupRequestDto signupRequestDto){
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/users/login")
    public BaseResponse<TokenResponseDto> login(HttpServletResponse response, @RequestBody AuthRequestDto authRequestDto) {
        return BaseResponse.ok(userService.login(response, authRequestDto));
    }

    @PostMapping("/users/refresh")
    public BaseResponse<TokenResponseDto> refreshAccessToken(@CookieValue("Refresh-Token") String refreshToken) {
        return BaseResponse.ok(userService.refreshAccessToken(refreshToken));
    }

    @PostMapping("/users/logout")
    public BaseResponse<Void> logout(@CookieValue("Refresh-Token") String refreshToken) {
        userService.logout(refreshToken);
        return BaseResponse.ok();
    }

    @PutMapping("/users/independence-auth")
    public BaseResponse<Void> authenticateIndependence() {
        userService.addIndependenceRole();
        return BaseResponse.ok();
    }
}
