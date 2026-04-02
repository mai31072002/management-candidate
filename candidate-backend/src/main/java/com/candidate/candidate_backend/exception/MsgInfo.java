package com.candidate.candidate_backend.exception;

import lombok.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MsgInfo {
    private String msgId;

      private List<String> msgArgs = new ArrayList<>();

      /**
       * @param msgId String
       * @param msgArgs List-String
       * @return MsgInfo
       */
      public static MsgInfo by(String msgId, List<String> msgArgs) {
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setMsgId(msgId);
        msgInfo.setMsgArgs(msgArgs);
        return msgInfo;
      }

      /**
       * @param msgId String
       * @return MsgInfo
       */
      public static MsgInfo by(String msgId) {
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setMsgId(msgId);
        return msgInfo;
      }
}

