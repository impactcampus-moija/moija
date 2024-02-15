package impact.moija.validator;

import impact.moija.domain.mentoring.MentoringCategory;
import java.util.StringTokenizer;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MentoringCategoryValidator implements ConstraintValidator<CheckMentoringCategory, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 1. null 처리
        if(value == null) {
            return true;
        }

        StringTokenizer st = new StringTokenizer(value, ",");

        while(st.hasMoreTokens()) {
            String name = st.nextToken();

            // 2. 빈 문자열 및 공백 처리
            if (name.isEmpty() || name.isBlank()) {
                return false;
            }

            // 3. 오타 처리
            return MentoringCategory.isExist(name);
        }

        return true;
    }
}
