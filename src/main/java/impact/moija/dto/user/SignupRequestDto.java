package impact.moija.dto.user;

import impact.moija.domain.user.Gender;
import impact.moija.domain.user.Location;
import impact.moija.domain.user.User;
import impact.moija.domain.user.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Getter
@Builder
public class SignupRequestDto {
    String email;
    String password;
    String nickname;
    String birthday;
    String location;
    String gender;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .birthday(LocalDate.parse(birthday))
                .location(Location.findByName(location))
                .gender(Gender.findByName(gender))
                .role(UserRole.ROLE_USER)
                .build();
    }
}
