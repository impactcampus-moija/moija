package impact.moija.dto.user;

import impact.moija.domain.user.User;
import impact.moija.domain.user.UserRole;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class AuthRequestDto {
    String email;
    String password;
}
