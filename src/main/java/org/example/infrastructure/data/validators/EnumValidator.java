package org.example.infrastructure.data.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.annotations.InEnumValues;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<InEnumValues, String> {
    private String[] enumConstants;
    private String availableValuesStr;

    @Override
    public void initialize(InEnumValues constraintAnnotation) {
        this.enumConstants = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
        this.availableValuesStr = String.join(", ", enumConstants);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || Arrays.asList(enumConstants).contains(value)) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Invalid value. Available values: " + availableValuesStr
        ).addConstraintViolation();

        return false;
    }
}
