package com.example.demo.config;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BidPriceValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BidPrice {
    String message() default "3만원이상 천원 단위만 가능";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
