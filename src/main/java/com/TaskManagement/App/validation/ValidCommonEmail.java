package com.TaskManagement.App.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CommonEmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCommonEmail {
    String message() default "Invalid or uncommon email domain";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}