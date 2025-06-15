package com.TaskManagement.App.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommonEmailValidator implements ConstraintValidator<ValidCommonEmail, String> {

    private static final List<String> COMMON_DOMAINS = Arrays.asList(
            "gmail.com", "hotmail.com", "outlook.com", "yahoo.com",
            "icloud.com", "aol.com", "live.com", "protonmail.com", "mail.com"
    );

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }

        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return COMMON_DOMAINS.contains(domain);
    }
}