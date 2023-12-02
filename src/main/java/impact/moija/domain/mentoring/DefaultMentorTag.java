package impact.moija.domain.mentoring;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultMentorTag {
    JOB("일자리"),
    RESIDENCE("주거"),
    ECONOMY("경제"),
    LIFE("생활/지원");

    private final String name;

}
