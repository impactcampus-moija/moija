package impact.moija.domain.user;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성")
    ;

    private final String name;

    public static Gender findByName(final String name) {
        for(Gender gender : Gender.values()) {
            if(gender.name.equals(name)) {
                return gender;
            }
        }
        throw new ApiException(MoijaHttpStatus.INVALID_GENDER_NAME);
    }
}
