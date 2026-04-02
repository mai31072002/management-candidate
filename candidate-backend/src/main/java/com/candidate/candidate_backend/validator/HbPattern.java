package com.candidate.candidate_backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = HbPatternConstraintValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Repeatable(HbPattern.List.class)
public @interface HbPattern {

  /** */
  int index() default 0;
  /** */
  String name() default "";
  /** */
  String regexp();
  /** */
  String regexpTextMessage() default "";
  /** */
  String message() default "common.formula.invalid";
  /** */
  Class<?>[] groups() default {};
  /** */
  Class<? extends Payload>[] payload() default {};

  @Target({METHOD, FIELD})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    HbPattern[] value();
  }
}
