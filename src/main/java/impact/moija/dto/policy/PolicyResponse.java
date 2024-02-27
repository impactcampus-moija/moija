package impact.moija.dto.policy;

import impact.moija.domain.policy.Policy;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PolicyResponse {
    private Long id;
    private String number;
    private String type;
    private String location;
    private String name;
    private String introduction;
    private String content;
    private Integer minAge;
    private Integer maxAge;
    private String major;
    private String employment;
    private String special;
    private String period;
    private String url;

    public static PolicyResponse of(Policy policy) {
        String locationName = null;
        if(policy.getLocation() != null) {
            locationName = policy.getLocation().getName();
        }
        return PolicyResponse.builder()
                .id(policy.getId())
                .number(policy.getNumber())
                .type(policy.getType().getName())
                .location(locationName)
                .name(policy.getName())
                .introduction(policy.getIntroduction())
                .content(policy.getContent())
                .minAge(policy.getMinAge())
                .maxAge(policy.getMaxAge())
                .major(policy.getMajor())
                .employment(policy.getEmployment())
                .special(policy.getSpecial())
                .period(policy.getPeriod())
                .url(policy.getUrl())
                .build();
    }
}
