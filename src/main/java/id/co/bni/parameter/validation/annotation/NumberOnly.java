package id.co.bni.parameter.validation.annotation;

import id.co.bni.parameter.validation.NumberOnlyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumberOnlyValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberOnly {
    String message() default "{Invalid number}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
