package com.candidate.candidate_backend.validator;

import com.candidate.candidate_backend.config.MsgTranslator;
import com.candidate.candidate_backend.entity.DtbUser;
import com.candidate.candidate_backend.repositorry.UserRepository;
import com.candidate.candidate_backend.util.Helper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class CheckExistedUserConstraintValidator
    implements ConstraintValidator<CheckExistedUser, String> {

  private CheckExistedUser validation;

  @Autowired private UserRepository userRepository;

  @Override
  public void initialize(CheckExistedUser constraint) {
    this.validation = constraint;
  }

  private String getMessage() {
    return MsgTranslator.toLocale(validation.message());
  }

  @Override
  public boolean isValid(String userId, ConstraintValidatorContext cxt) {
    userId = Helper.convertMsisdn(userId);
    boolean valid = true;
    if (userId.isEmpty()) {
      return true;
    }

    userId = userId.toLowerCase();
    try {
      UUID uuidUserId = UUID.fromString(Helper.convertMsisdn(userId));
      Optional<DtbUser> user =
          userRepository.findFirstByUserIdAndIsDeletedIsFalse(uuidUserId);
      if (user.isPresent() && !validation.notExisted()) {
        valid = false;
      }
      if (!user.isPresent() && validation.notExisted()) {
        valid = false;
      }
    } catch (NoSuchElementException e) {
      return true;
    }
    if (!valid) {
      cxt.disableDefaultConstraintViolation();
      cxt.buildConstraintViolationWithTemplate(this.getMessage()).addConstraintViolation();
      return false;
    }
    return true;
  }
}
