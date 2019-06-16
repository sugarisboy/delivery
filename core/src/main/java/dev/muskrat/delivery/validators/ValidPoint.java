package dev.muskrat.delivery.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPointValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPoint {

    String message() default "One or more point is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}