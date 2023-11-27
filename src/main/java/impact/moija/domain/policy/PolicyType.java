package impact.moija.domain.policy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PolicyType {
    JOB("일자리 분야", "023010"),
    RESIDENCE("주거 분야", "023020"),
    EDUCATION("교육 분야", "023030"),
    WELFARE("복지/문화 분야", "023040"),
    PARTICIPATION("참여/권리 분야", "023050");

    private final String name;
    private final String code;

    public static PolicyType findByCode(String code) {
        for(PolicyType policy : PolicyType.values()) {
            if(policy.getCode().equals(code)) {
                return policy;
            }
        }

        return null;
    }
}
