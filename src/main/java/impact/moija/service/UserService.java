package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.user.Location;
import impact.moija.domain.user.RefreshToken;
import impact.moija.domain.user.User;
import impact.moija.dto.jwt.TokenResponseDto;
import impact.moija.dto.user.AuthRequestDto;
import impact.moija.dto.user.SignupRequestDto;
import impact.moija.jwt.TokenProvider;
import impact.moija.jwt.TokenType;
import impact.moija.repository.user.RefreshTokenRepository;
import impact.moija.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public User signup(SignupRequestDto signupRequestDto) {
        User user = signupRequestDto.toEntity(passwordEncoder);

        return userRepository.save(user);
    }

    @Transactional
    public TokenResponseDto login(final HttpServletResponse response, final AuthRequestDto authRequestDto) {
        final Authentication authentication = authenticate(authRequestDto);

        final TokenResponseDto accessToken = tokenProvider.generateTokenResponse(TokenType.ACCESS_TOKEN, authentication);

        final TokenResponseDto refreshToken = tokenProvider.generateTokenResponse(TokenType.REFRESH_TOKEN, authentication);
        createRefreshToken(refreshToken);

        setRefreshTokenCookie(response, refreshToken);

        return accessToken;
    }

    private Authentication authenticate(final AuthRequestDto authRequestDto) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequestDto.getEmail(), authRequestDto.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private void createRefreshToken(TokenResponseDto refreshTokenDto) {
        Long userId = getLoginMemberId();

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElse(RefreshToken.builder()
                        .user(
                                User.builder().id(userId).build()
                        )
                        .build());

        refreshToken.updateToken(refreshTokenDto.getToken());
        refreshTokenRepository.save(refreshToken);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, TokenResponseDto refreshTokenDto) {

        String cookieValue = "Bearer " + refreshTokenDto.getToken();
        cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8);

        Cookie cookie = new Cookie(TokenType.REFRESH_TOKEN.getHeader(), cookieValue);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) refreshTokenDto.getExpiredAt() / 1000);
        response.addCookie(cookie);
    }


    @Transactional
    public void logout(final String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.UNAUTHORIZED));

        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshAccessToken(final String refreshTokenCookie) {
        String refreshToken = refreshTokenCookie.substring(7);
        tokenProvider.validateToken(TokenType.REFRESH_TOKEN, refreshToken);

        final User user = userRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new ApiException(MoijaHttpStatus.INVALID_TOKEN)
        );

        return tokenProvider.generateTokenResponse(TokenType.ACCESS_TOKEN, user);
    }

    public Long getLoginMemberId() {
        try {
            return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ApiException(MoijaHttpStatus.INVALID_EMAIL_OR_PASSWORD)
        );
    }
}
