package impact.moija.validator;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD}) // 필드에만 어노테이션 선언 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임 동안에만 어노테이션
@Constraint(validatedBy = MentoringCategoryValidator.class)
@Documented // JavaDoc 생성 시 Annotation에 대한 정보도 함께 생성
public @interface CheckMentoringCategory {

    //  JSR-303 표준 어노테이션들이 갖는 공통 속성
    String message() default "올바르지 않은 멘토링 카테고리입니다."; // 유효성 검증에 실패한 경우 반환할 메시지
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
