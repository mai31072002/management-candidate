package com.candidate.candidate_backend.validator;

import com.candidate.candidate_backend.config.MsgTranslator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HbPatternConstraintValidator implements ConstraintValidator<HbPattern, String> {

  private HbPattern validation;

  private Pattern pattern;

  @Override
  public void initialize(HbPattern validation) {
    this.validation = validation;
    if (validation.regexpTextMessage().length() == 0) {
      this.pattern = Pattern.compile(validation.regexp());
    } else {
      this.pattern = Pattern.compile(MsgTranslator.toLocale(validation.regexpTextMessage()));
    }
  }

  private String getMessage() {
    return String.format(
        MsgTranslator.toLocale(validation.message()), MsgTranslator.toLocale(validation.name()));
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext cxt) {
    if (value == null || value.isEmpty()) {
      return true;
    }
    Matcher m = pattern.matcher(value);
    if (m.matches()) {
      return true;
    }
    cxt.disableDefaultConstraintViolation();
    cxt.buildConstraintViolationWithTemplate(this.getMessage()).addConstraintViolation();
    return false;
  }
}
