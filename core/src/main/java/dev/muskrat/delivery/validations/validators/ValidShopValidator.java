package dev.muskrat.delivery.validations.validators;

import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.Optional;

@RequiredArgsConstructor
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class ValidShopValidator implements ConstraintValidator<ValidShop, Long> {

    private final ShopRepository shopRepository;

    @Override
    public boolean isValid(Long shopId, ConstraintValidatorContext context) {
        if (shopId == null)
            return error(context, "ShopId is null");
        Optional<Shop> byId = shopRepository.findById(shopId);
        if (!byId.isPresent())
            return error(context, "Shop with id " + shopId + " not found");

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
