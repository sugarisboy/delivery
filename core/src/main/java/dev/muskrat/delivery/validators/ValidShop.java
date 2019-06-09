package dev.muskrat.delivery.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidShopValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidShop {

    String message() default "Shop is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
