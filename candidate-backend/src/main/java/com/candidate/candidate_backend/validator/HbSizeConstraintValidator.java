package com.candidate.candidate_backend.validator;

import com.candidate.candidate_backend.config.MsgTranslator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HbSizeConstraintValidator implements ConstraintValidator<HbSize, String> {

  private HbSize validation;

  private int min;
  private int max;

  @Override
  public void initialize(HbSize validation) {
    this.validation = validation;
    min = validation.min();
    max = validation.max();
  }

  private String getMessage() {
    return String.format(
        MsgTranslator.toLocale(validation.message()), MsgTranslator.toLocale(validation.name()));
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext cxt) {
    if (value == null) {
      return true;
    }
    if (value.length() < min || value.length() > max) {
      cxt.disableDefaultConstraintViolation();
      cxt.buildConstraintViolationWithTemplate(this.getMessage()).addConstraintViolation();
      return false;
    }
    return true;
  }
}
