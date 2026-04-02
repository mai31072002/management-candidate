package com.candidate.candidate_backend.validator;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = HbRequiredConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface HbRequired {

  /** */
  int index() default 0;
  /** */
  String type() default "";
  /** */
  String name() default "";
  /** */
  String message() default "common.formula.require";

  String code() default "";
  /** */
  Class<?>[] groups() default {};
  /** */
  Class<? extends Payload>[] payload() default {};
}
