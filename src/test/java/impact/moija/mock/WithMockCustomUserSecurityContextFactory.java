package impact.moija.mock;

import impact.moija.domain.user.User;
import impact.moija.domain.user.UserRole;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Set<UserRole> roles = Arrays.stream(annotation.roles()).map(UserRole::valueOf).collect(Collectors.toSet());

        User principal = User.builder()
                .id(annotation.id())
                .email(annotation.email())
                .password(annotation.password())
                .roles(roles)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities()
        );
        context.setAuthentication(auth);

        return context;
    }
}
