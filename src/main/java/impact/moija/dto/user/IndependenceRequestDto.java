package impact.moija.dto.user;

import impact.moija.domain.user.Independence;
import impact.moija.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IndependenceRequestDto {
    int independenceYear;

    public Independence toEntity(final long loginUserId) {
        return Independence.builder()
                .user(User.builder().id(loginUserId).build())
                .independenceYear(independenceYear)
                .activate(false)
                .build();
    }
}
