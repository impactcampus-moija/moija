package impact.moija.domain.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultCategory {
    ALL("전체"),
    RESIDENCE("주거"),
    EMPLOYMENT("취업"),
    CAREER("진로"),
    LIFESTYLE("생활"),
    ECONOMY("경제"),
    FREEDOM("자유");

    private final String name;
}
