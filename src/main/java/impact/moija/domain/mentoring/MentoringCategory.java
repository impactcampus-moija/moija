package impact.moija.domain.mentoring;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MentoringCategory {
    JOB("일자리"),
    RESIDENCE("주거"),
    ECONOMY("경제"),
    LIVING("생활/지원");

    private final String name;

    public static boolean isExist(String name) {
        for (MentoringCategory category : MentoringCategory.values()) {
            if (category.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
