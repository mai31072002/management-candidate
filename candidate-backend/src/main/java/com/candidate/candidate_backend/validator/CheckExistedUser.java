package com.candidate.candidate_backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckExistedUserConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckExistedUser {
  /** */
  int index() default 0;
  /** */
  boolean notExisted() default false;
  /** */
  String name() default "";
  /** */
  String message();
  /** */
  Class<?>[] groups() default {};
  /** */
  Class<? extends Payload>[] payload() default {};
}
