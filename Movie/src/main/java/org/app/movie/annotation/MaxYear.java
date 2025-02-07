package org.app.movie.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrentYearValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxYear {
    String message() default "Year must not be greater than the current year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
