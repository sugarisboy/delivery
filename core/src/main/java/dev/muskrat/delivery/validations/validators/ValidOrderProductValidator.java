package dev.muskrat.delivery.validations.validators;

import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dao.ProductRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.Optional;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@RequiredArgsConstructor
public class ValidOrderProductValidator implements ConstraintValidator<ValidOrderProduct, Long> {

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(Long productId, ConstraintValidatorContext context) {
        if (productId == null)
            return error(context, "ProductId is null");
        Optional<Product> byId = productRepository.findById(productId);
        if (!byId.isPresent())
            return error(context,"Product with id " + productId + " not found");
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
