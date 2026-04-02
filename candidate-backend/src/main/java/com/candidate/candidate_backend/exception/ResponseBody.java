package com.candidate.candidate_backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.*;

@Getter
@Setter
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody {
    private String type;
      private String title;
      private int status;
      private String internal;
      private List<MsgInfo> msgList = new ArrayList<>();
      private Map validMsgList = new HashMap<String, List<String>>();

      /**
       * @param req HttpServletRequest
       * @param e Exception
       * @param status HttpStatus
       * @return ResponseBody
       */
      public static ResponseBody of(HttpServletRequest req, Exception e, HttpStatus status) {
        ResponseBody body = new ResponseBody();
        log.error(e.getMessage());
        // body.setTitle(e.getMessage());
        body.setStatus(status.value());
        body.setInternal(req.getRequestURI());

        return body;
      }

      /**
       * @param req HttpServletRequest
       * @param message String
       * @param status HttpStatus
       * @return ResponseBody
       */
      public static ResponseBody of(HttpServletRequest req, String message, HttpStatus status) {
        ResponseBody body = new ResponseBody();
        log.error(message);
        body.setTitle(req.getRequestURI());
        body.setStatus(status.value());
        body.setInternal(req.getRequestURI());

        return body;
      }

      public void addMsg(MsgInfo msg) {
        msgList.add(msg);
      }
}
