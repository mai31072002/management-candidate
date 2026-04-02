package com.candidate.candidate_backend.validator;

import com.candidate.candidate_backend.config.MsgTranslator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class HbSizeListConstraintValidator implements ConstraintValidator<HbSizeList, List> {

  private HbSizeList validation;

  private int min;
  private int max;

  @Override
  public void initialize(HbSizeList validation) {
    this.validation = validation;
    min = validation.min();
    max = validation.max();
  }

  private String getMessage() {
    return String.format(
        MsgTranslator.toLocale(validation.message()),
        MsgTranslator.toLocale(validation.name()),
        validation.min(),
        validation.max());
  }

  @Override
  public boolean isValid(List list, ConstraintValidatorContext cxt) {
    if (list == null || list.size() == 0) {
      return true;
    }
    if (list.size() < min || list.size() > max) {
      cxt.disableDefaultConstraintViolation();
      cxt.buildConstraintViolationWithTemplate(this.getMessage()).addConstraintViolation();
      return false;
    }
    return true;
  }
}
