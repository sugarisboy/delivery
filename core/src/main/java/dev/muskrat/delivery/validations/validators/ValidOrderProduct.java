package dev.muskrat.delivery.validations.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidOrderProductValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderProduct {

    String message() default "Product is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
