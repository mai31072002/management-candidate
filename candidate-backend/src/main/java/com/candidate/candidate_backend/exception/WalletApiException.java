package com.candidate.candidate_backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WalletApiException extends jakarta.validation.ValidationException {
    private HttpStatus walletApiStatusCode;
    private List<MsgInfo> msgList = new ArrayList<>();

    /**
    * @author datpv
    * @date 2021-05-18 21:42:58
    * @param msgId
    * @param msgArgs
    * @return
    */
    public WalletApiException(String msgId, List<String> msgArgs) {
      this.msgList.add(MsgInfo.by(msgId, msgArgs));
    }

    /**
    * @author datpv
    * @date 2021-05-18 21:43:02
    * @return
    */
    public WalletApiException() {
      super();
    }

    /**
    * @author datpv
    * @date 2021-05-18 21:43:06
    * @param message
    * @param statusCode
    * @return
    */
    public WalletApiException(String message, HttpStatus statusCode) {
      super(message);
      walletApiStatusCode = statusCode;
    }

    /**
    * @author datpv
    * @date 2021-05-18 21:43:10
    * @param msgId
    * @param msgArgs
    * @return
    */
    public WalletApiException add(String msgId, List<String> msgArgs) {
      this.msgList.add(MsgInfo.by(msgId, msgArgs));
      return this;
    }

    /**
    * @author datpv
    * @date 2021-05-18 21:43:15
    * @param message
    * @param cause
    * @return
    */
    public WalletApiException(String message, Throwable cause) {
      super(message, cause);
    }

    /**
    * @author datpv
    * @date 2021-05-18 22:18:13
    * @param cause
    * @return
    */
    public WalletApiException(Throwable cause) {
      super(cause);
    }
}
