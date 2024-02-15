package impact.moija.validator;

import impact.moija.domain.mentoring.MentoringStatus;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MentoringStatusValidator implements ConstraintValidator<CheckMentoringStatus, MentoringStatus> {

    private String[] values;

    @Override
    public void initialize(CheckMentoringStatus constraintAnnotation) {
        values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MentoringStatus value, ConstraintValidatorContext context) {
        if (value == null) return false;

        String stringValue = value.name();

        // 지정한 MentoringStatus 중 하나라도 있으면 통과
        boolean isExist = false;
        for (String s : values) {
            if (s.equals(stringValue)) {
                isExist = true;
                break;
            }
        }

        return isExist;
    }
}
