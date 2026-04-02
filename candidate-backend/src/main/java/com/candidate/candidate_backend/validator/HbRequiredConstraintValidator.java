package com.candidate.candidate_backend.validator;

import com.candidate.candidate_backend.config.MsgTranslator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class HbRequiredConstraintValidator implements ConstraintValidator<HbRequired, Object> {

  private HbRequired validation;

  @Override
  public void initialize(HbRequired validation) {
    this.validation = validation;
  }

  private String getMessage() {
    return String.format(
        MsgTranslator.toLocale(validation.message()),
        MsgTranslator.toLocale(validation.name())
    );
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext cxt) {
    boolean valid = true;
    if (value == null) {
      valid = false;
    } else if (value instanceof String) {
      if (((String) value).trim().isEmpty()) {
        valid = false;
      }
    } else if (value instanceof List) {
      if (((List) value).size() == 0) {
        valid = false;
      }
    } else if (value instanceof String[]) {
      String[] array = (String[]) value;
      if (array.length == 0) valid = false;
    }

    if (!valid) {
      cxt.disableDefaultConstraintViolation();
      cxt.buildConstraintViolationWithTemplate(this.getMessage()).addConstraintViolation();
      return false;
    }
    return true;
  }
}

