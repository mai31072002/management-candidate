package com.candidate.candidate_backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppValidationException extends jakarta.validation.ValidationException {
    private String type;

  private List<MsgInfo> msgList = new ArrayList<>();

  /**
   * @author datpv
   * @date 2021-05-18 21:41:16
   * @param msgId
   * @param msgArgs
   * @return
   */
  public AppValidationException(String msgId, List<String> msgArgs) {
    this.msgList.add(MsgInfo.by(msgId, msgArgs));
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:41:20
   * @return
   */
  public AppValidationException() {
    super();
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:41:34
   * @param message
   * @return
   */
  public AppValidationException(String message) {
    super(message);
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:41:40
   * @param message
   * @param type
   * @return
   */
  public AppValidationException(String message, String type) {
    super(message);
    this.type = type;
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:41:45
   * @param msgId
   * @param msgArgs
   * @return
   */
  public AppValidationException add(String msgId, List<String> msgArgs) {
    this.msgList.add(MsgInfo.by(msgId, msgArgs));
    return this;
  }

  /**
   * @author datpv
   * @date 2021-05-18 21:41:51
   * @param message
   * @param cause
   * @return
   */
  public AppValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @author datpv
   * @date 2021-05-18 22:00:33
   * @param cause
   * @return
   */
  public AppValidationException(Throwable cause) {
    super(cause);
  }
}
