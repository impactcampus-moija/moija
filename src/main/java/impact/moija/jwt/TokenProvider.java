package impact.moija.jwt;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.user.User;
import impact.moija.dto.jwt.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String TOKEN_TYPE = "Bearer";
    private static final String AUTHORITY_KEY = "auth";

    @Value("${jwt.secret-key.access}")
    private String accessTokenSecretKey;

    @Value("${jwt.secret-key.refresh}")
    private String refreshTokenSecretKey;
    private Key accessTokenKey;
    private Key refershTokenKey;

    @PostConstruct
    public void init() {
        accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecretKey));
        refershTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecretKey));
    }

    public TokenResponseDto generateTokenResponse(TokenType tokenType, Authentication authentication) {
        return generateTokenResponse(
                tokenType,
                Long.valueOf(authentication.getName()),
                joinAuthorities(authentication.getAuthorities())
        );
    }

    public TokenResponseDto generateTokenResponse(TokenType tokenType, User user) {
        return generateTokenResponse(tokenType, user.getId(), joinAuthorities(user.getAuthorities()));
    }

    private String joinAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private TokenResponseDto generateTokenResponse(TokenType tokenType, Long memberId, String authority) {
        long nowMillisecond = new Date().getTime();
        return TokenResponseDto.builder()
                .token(generateToken(tokenType, memberId, authority, nowMillisecond))
                .expiredAt((nowMillisecond + tokenType.getValidMillisecond()))
                .build();
    }

    private String generateToken(TokenType tokenType, Long memberId, String authority, long nowMillisecond) {
        return Jwts.builder()
                .setIssuer("moija")
                .setSubject(memberId.toString())
                .setExpiration(new Date(nowMillisecond + tokenType.getValidMillisecond()))
                .claim(AUTHORITY_KEY, authority)
                .signWith(getKey(tokenType), SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token, accessTokenKey);

        if (ObjectUtils.isEmpty(claims.get(AUTHORITY_KEY))) {
            throw new ApiException(MoijaHttpStatus.INVALID_TOKEN);
        }

        Collection<? extends GrantedAuthority> authority = parseAuthority(claims.get(AUTHORITY_KEY).toString());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authority);
    }

    private Collection<? extends GrantedAuthority> parseAuthority(String authority) {
        return Arrays.stream(authority.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public void validateToken(TokenType tokenType, String token) {
        parseTokenClaims(tokenType, token);
    }

    public Claims parseTokenClaims(TokenType tokenType, String token) {
        return parseClaims(token, getKey(tokenType));
    }

    private Claims parseClaims(String token, Key key) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ApiException(MoijaHttpStatus.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new ApiException(MoijaHttpStatus.INVALID_TOKEN);
        }
    }

    public String getAccessToken(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElseThrow(() ->
                new ApiException(MoijaHttpStatus.UNAUTHORIZED));

        if (!StringUtils.hasText(token) || !StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE)) {
            throw new ApiException(MoijaHttpStatus.INVALID_TOKEN);
        }

        return token.substring(7);
    }

    private Key getKey(TokenType tokenType) {
        if (tokenType == TokenType.ACCESS_TOKEN) {
            return accessTokenKey;
        } else if (tokenType == TokenType.REFRESH_TOKEN) {
            return refershTokenKey;
        }
        return null;
    }
}