package impact.moija.repository.user;

import impact.moija.domain.user.Gender;
import impact.moija.domain.user.RefreshToken;
import impact.moija.domain.user.User;
import impact.moija.domain.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("가입된 사용자를 이메일을 통해 조회한다.")
    void findSavedUserByEmail() {
        final User user = createUser();
        userRepository.save(user);

        final Optional<User> findUser = userRepository.findByEmail("moiza@gmail.com");

        assertThat(findUser).isNotEmpty();
        assertThat(findUser.get().getNickname()).isEqualTo(user.getNickname());
    }

    @Test
    @DisplayName("가입된 사용자를 리프레시 토큰을 이용해 조회한다.")
    void findSavedUserByRefreshToken() {
        final User user = createUser();
        userRepository.save(user);
        final RefreshToken refreshToken = createRefreshToken(user);
        refreshTokenRepository.save(refreshToken);

        final Optional<User> findUser = userRepository.findByRefreshToken("token");

        assertThat(findUser).isNotEmpty();
        assertThat(findUser.get().getNickname()).isEqualTo(user.getNickname());
    }


    private User createUser() {
        return User.builder()
                .email("moiza@gmail.com")
                .password("expassword")
                .nickname("moiza")
                .birthday(LocalDate.of(1999,2,4))
                .gender(Gender.MALE)
                .roles(Collections.singleton(UserRole.ROLE_USER))
                .build();
    }

    private RefreshToken createRefreshToken(final User user) {
        return RefreshToken.builder()
                .user(user)
                .token("token")
                .build();
    }
}
