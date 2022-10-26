package com.example.demo.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BidPriceValidator implements ConstraintValidator<BidPrice, Long> {
    @Override
    public void initialize(BidPrice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value != null && value>=30000 && value%1000==0;
    }
}
