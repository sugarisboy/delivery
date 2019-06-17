package dev.muskrat.delivery.validators;

import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.List;

@RequiredArgsConstructor
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class ValidPointValidator implements ConstraintValidator<ValidPoint, List<Double>> {

    @Override
    public boolean isValid(List<Double> list, ConstraintValidatorContext context) {
        if (list.size() % 3 != 0)
            return error(context, "Each point must be declared array of latitude, longitude and altitude");
        return true;
    }

    private boolean error(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();
        return false;
    }
}