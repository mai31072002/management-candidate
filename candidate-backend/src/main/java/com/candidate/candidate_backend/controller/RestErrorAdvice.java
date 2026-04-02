package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.Common.ConstantsConfig;
import com.candidate.candidate_backend.config.ConstantProperties;
import com.candidate.candidate_backend.config.MsgTranslator;
import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.enums.ValidateType;
import com.candidate.candidate_backend.exception.*;
import com.candidate.candidate_backend.exception.ResponseBody;
import com.candidate.candidate_backend.util.Helper;
import com.candidate.candidate_backend.validator.dto.ValidMsgList;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.BindException;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class RestErrorAdvice {
    @Autowired
    private ConstantProperties constantProperties;

    private static ResourceBundleMessageSource messageSource;

  /** @param binder WebDataBinder */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringtrimmer);
    Object obj = binder.getTarget();
    if (obj == null) {
      return;
    }
    List<Field> fields = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredFields()));
    if (obj.getClass().getSuperclass() != null) {
      fields.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
    }
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object value = field.get(obj);
        if (value instanceof String) {
          field.set(obj, ((String) value).replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
        } else if (value instanceof Collection<?>) {
          Collection<?> list = (Collection<?>) value;

          for (Object el : list) {

            if (el == null) continue;

            // tránh reflection vào String / Integer / etc
            if (el.getClass().getPackageName().startsWith("java")) {
              continue;
            }

            List<Field> fieldChilds =
                    new ArrayList<>(Arrays.asList(el.getClass().getDeclaredFields()));

            if (el.getClass().getSuperclass() != null) {
              fieldChilds.addAll(Arrays.asList(el.getClass().getSuperclass().getDeclaredFields()));
            }

            for (Field fieldChild : fieldChilds) {
              fieldChild.setAccessible(true);

              Object valueChild = fieldChild.get(el);

              if (valueChild instanceof String str) {
                fieldChild.set(el, str.replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
              }
            }
          }
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @author datpv10
   * @date 2021-05-18 20:30:55
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(ServletRequestBindingException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody>
      handleServletRequestBindingException(
          HttpServletRequest req, ServletRequestBindingException e) {
    log.warn(e.getMessage(), e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv10
   * @date 2021-05-18 20:39:12
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(HttpMediaTypeException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleHttpMediaTypeException(
      HttpServletRequest req, HttpMediaTypeException e) {
    log.warn(e.getMessage(), e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:04
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<ResponseBody>
      handleOptimisticLockingFailureException(
          HttpServletRequest req, OptimisticLockingFailureException e) {
    log.warn(e.getMessage(), e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ResponseBody body =
        ResponseBody.of(req, e, status);
    body.addMsg(MsgInfo.by(MessageId.OPTIMISTIC_LOCK));
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:10
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleAccessDeniedException(
      HttpServletRequest req, AccessDeniedException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.FORBIDDEN;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<CommonsRep> handleUsernameNotFoundException(
      HttpServletRequest req, UsernameNotFoundException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    CommonsRep commonRes =
        CommonsRep.builder()
            .status(status.value())
            .message(MsgTranslator.toLocale(e.getMessage()))
            .build();

    return new ResponseEntity<>(commonRes, status);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ResponseBody> handleResponseStatusException(
      HttpServletRequest req, ResponseStatusException e) {
    HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
    ResponseBody body = ResponseBody.of(req, e, status);
    body.setTitle(e.getReason());
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:17
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleEntityNotFoundException(
      HttpServletRequest req, EntityNotFoundException e) {
    log.warn(e.getMessage(), e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:22
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleConstraintViolationException(
      HttpServletRequest req, ConstraintViolationException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @param req HttpServletRequest
   * @param e javax.validation.ConstraintViolationException
   * @return ResponseEntity-ResponseBody
   */
  @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
  public static ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody>
      handleConstraintViolationException(
          HttpServletRequest req, jakarta.validation.ConstraintViolationException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    ValidMsgList errorList = new ValidMsgList();
    for (ConstraintViolation error : e.getConstraintViolations()) {
      if (error.getConstraintDescriptor().getAttributes().containsKey("index")) {
        Object obj = error.getConstraintDescriptor().getAttributes().get("index");
        if (obj instanceof Integer) {
          errorList.putDeepField(
              error.getPropertyPath().toString(),
              error.getMessageTemplate(),
              (Integer) obj,
              ValidateType.NORMAL);
          continue;
        }
      }
    }
    body.setValidMsgList(errorList.getMap());
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:30
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(AppValidationException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleValidationException(
      HttpServletRequest req, AppValidationException e) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    // Check message type
    if (e.getType() == ValidateType.SHOW_IN_TITLE) {
      body.setTitle("ValidationException");
    }
    ValidMsgList errorList = new ValidMsgList();
    errorList.putDeepField("errorCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
    errorList.putDeepField("errorDesc", e.getMessage());
    body.setType(e.getType());
    body.setValidMsgList(errorList.getMap());
    e.getMsgList().stream()
        .forEach(msg -> body.addMsg(MsgInfo.by(msg.getMsgId(), msg.getMsgArgs())));

    body.getMsgList().stream().forEach(msg -> log.warn("warn information : " + msg));

    return new ResponseEntity<>(body, status);
  }

  @ExceptionHandler(UserResourceLimitException.class)
  public ResponseEntity<CommonsRep> handleUserResourceLimitException(
      HttpServletRequest req, UserResourceLimitException e) {

    HttpStatus status = HttpStatus.UPGRADE_REQUIRED;

    CommonsRep commonRes =
        CommonsRep.builder()
            .status(ConstantsConfig.LIMIT_RESOURCE_ERROR)
            .message(MsgTranslator.toLocale("valid.limitResource"))
            .build();

    return new ResponseEntity<>(commonRes, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:38
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody>
      handleHttpMessageNotReadableException(
          HttpServletRequest req, HttpMessageNotReadableException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:42
   * @param arguments
   * @return
   */
  private ValidateType checkNoIndexFieldError(Object[] arguments) {
    for (Object arg : arguments) {
      if (arg instanceof MessageSourceResolvable) {
        MessageSourceResolvable argMsg = (MessageSourceResolvable) arg;
        if (argMsg.getDefaultMessage().equalsIgnoreCase(ValidateType.GLOBAL_OBJECT_IN_OBJECT)) {
          return ValidateType.GLOBALOBJINOBJ;
        } else if (argMsg
            .getDefaultMessage()
            .equalsIgnoreCase(ValidateType.PRIVATE_OBJECT_IN_OBJECT)) {
          return ValidateType.PRIVATEOBJINOBJ;
        } else if (argMsg.getDefaultMessage().equalsIgnoreCase(ValidateType.SHOW_IN_TITLE)) {
          return ValidateType.SHOWINTITLE;
        }
      }
    }
    return ValidateType.NORMAL;
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:49
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody>
      handleMethodArgumentNotValidException(
          HttpServletRequest req, MethodArgumentNotValidException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e.getBindingResult().toString(), status);
    ValidMsgList errorList = new ValidMsgList();
    List<FieldError> globalErrors = new ArrayList<>();
    for (FieldError error : e.getBindingResult().getFieldErrors()) {
      ValidateType type = this.checkNoIndexFieldError(error.getArguments());
      if (type == ValidateType.GLOBALOBJINOBJ) {
        globalErrors.add(error);
        continue;
      } else if (type == ValidateType.SHOWINTITLE) {
        body.setType(ValidateType.SHOW_IN_TITLE);
        body.setTitle(error.getDefaultMessage());
        continue;
      }
      if (error.getArguments().length > 1) {
        Object obj = error.getArguments()[1];
        if (obj instanceof Integer) {
          errorList.putDeepField(
              error.getField(),
              StringUtils.capitalize(error.getDefaultMessage()),
              (Integer) obj,
              type);
          continue;
        }
      }
      errorList.putDeepField(error.getField(), StringUtils.capitalize(error.getDefaultMessage()));
    }

    for (FieldError error : globalErrors) {
      String fieldName;
      if (error.getArguments().length > 1) {
        String errorFieldName =
            ((MessageSourceResolvable) error.getArguments()[1]).getDefaultMessage();
        fieldName = error.getField() + "." + constantProperties.getString(errorFieldName);
      } else {
        fieldName = error.getObjectName();
      }
      if (error.getArguments().length > 2) {
        Object obj = error.getArguments()[2];
        if (obj instanceof Integer) {
          errorList.putDeepField(
              fieldName, error.getDefaultMessage(), (Integer) obj, ValidateType.GLOBALOBJINOBJ);
          continue;
        }
      }
      errorList.putDeepField(fieldName, error.getDefaultMessage());
    }

    for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
      ValidateType type = this.checkNoIndexFieldError(error.getArguments());
      if (type == ValidateType.SHOWINTITLE) {
        body.setType(ValidateType.SHOW_IN_TITLE);
        body.setTitle(error.getDefaultMessage());
        continue;
      }
      String errorFieldName;
      if (error.getArguments().length > 1) {
        errorFieldName = ((MessageSourceResolvable) error.getArguments()[1]).getDefaultMessage();
      } else {
        errorFieldName = error.getObjectName();
      }
      if (error.getArguments().length > 2) {
        Object obj = error.getArguments()[2];
        if (obj instanceof Integer) {
          errorList.putDeepField(errorFieldName, error.getDefaultMessage(), (Integer) obj, type);
          continue;
        }
      }
      errorList.putDeepField(errorFieldName, error.getDefaultMessage());
    }
    body.setValidMsgList(errorList.getMap());
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:37:59
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleBindException(
      HttpServletRequest req, BindException e) {

    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:38:07
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleIoException(
      HttpServletRequest req, IOException e) {
    if (e.getMessage().contains("Broken pipe")) {
      log.error("client connection was unexpected closing. (broken pipe)");
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } else {
      return handleException(req, e);
    }
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:38:14
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(CannotCreateTransactionException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody>
      handleCannotCreateTransactionException(
          HttpServletRequest req, CannotCreateTransactionException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, "db connect failed", status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:38:26
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody>
      handleInvalidDataAccessResourceUsageException(
          HttpServletRequest req, InvalidDataAccessResourceUsageException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, "db sql error", status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:38:33
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(ResourceAccessException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleResourceAccessException(
      HttpServletRequest req, ResourceAccessException e) {
    log.warn(e.getMessage());
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, "rest connect failed", status);
    return new ResponseEntity<>(body, status);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:38:40
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(WalletApiException.class)
  public ResponseEntity<String> handleWalletApiException(
      HttpServletRequest req, WalletApiException e) {
    log.warn(e.getMessage());
    return new ResponseEntity<>(e.getMessage(), e.getWalletApiStatusCode());
  }

  /**
   * @author datpv
   * @date 2021-05-18 22:00:04
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleException(
      HttpServletRequest req, Exception e) {

    log.error(e.getMessage(), e);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    com.candidate.candidate_backend.exception.ResponseBody body =
        com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }

  // Business Exception (lỗi logic nghiệp vụ)
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<CommonsRep> handleBusiness(BusinessException exception) {
      CommonsRep response = Helper.getServerResponse(
              exception.getStatus(),
              exception.getMessage(),
              null
      );
      return ResponseEntity.status(exception.getStatus()).body(response);
  }

  /**
   * @author datpv
   * @date 2021-05-19 13:50:52
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<com.candidate.candidate_backend.exception.ResponseBody> handleNoHandlerFoundException(
      HttpServletRequest req, Exception e) {
    log.error(e.getMessage(), e);
    HttpStatus status = HttpStatus.NOT_FOUND;
    com.candidate.candidate_backend.exception.ResponseBody body = com.candidate.candidate_backend.exception.ResponseBody.of(req, e, status);
    return new ResponseEntity<>(body, status);
  }
}
