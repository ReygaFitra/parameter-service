package id.co.bni.parameter.validation;

import id.co.bni.parameter.validation.annotation.NumberOnly;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberOnlyValidator implements ConstraintValidator<NumberOnly, String> {

    @Override
    public void initialize(NumberOnly constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        return s != null && s.matches("\\d+");
    }
}
