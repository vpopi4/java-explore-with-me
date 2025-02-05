package ru.practicum.ewm.stats.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IpAddressValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIpAddress {
    String message() default "incorrect ip address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
