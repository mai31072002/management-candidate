package com.candidate.candidate_backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HbSizeConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HbSize {

  /** */
  int index() default 0;
  /** */
  String name() default "";
  /** */
  int min() default 0;
  /** */
  int max();
  /** */
  String message() default "valid.size";
  /** */
  Class<?>[] groups() default {};
  /** */
  Class<? extends Payload>[] payload() default {};
}
