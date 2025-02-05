package ru.practicum.ewm.stats.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.InetAddressValidator;

public class IpAddressValidator implements ConstraintValidator<ValidIpAddress, String> {
    private final InetAddressValidator validator = InetAddressValidator.getInstance();

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext context) {
        return ip != null && validator.isValid(ip);
    }
}
