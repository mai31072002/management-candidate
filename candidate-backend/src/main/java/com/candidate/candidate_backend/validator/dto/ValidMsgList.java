package com.candidate.candidate_backend.validator.dto;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.candidate.candidate_backend.enums.ValidateType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.util.Pair;

@Slf4j
public class ValidMsgList extends JSONObject {

  private void putMessage(JSONArray arr, String value, Integer orderIndex) {
    Pair<Integer, String> leaf = Pair.of(orderIndex, value);
    arr.put(leaf);
  }

  private void putDeepField(String[] keys, boolean[] isJsonObj, String value, Integer orderIndex) {
    if (keys.length <= 0) {
      return;
    }
    String key = keys[0];
    if (keys.length == 1) {
      JSONArray arr;
      if (this.has(key)) {
        arr = (JSONArray) this.get(key);
      } else {
        arr = new JSONArray();
        this.put(key, arr);
      }
      putMessage(arr, value, orderIndex);
      // arr.put(value);
      return;
    }
    if (isJsonObj[0] == false) { // Array
      ValidMsgList obj;
      if (this.has(key)) {
        obj = (ValidMsgList) this.get(key);
      } else {
        obj = new ValidMsgList();
        this.put(key, obj);
      }
      obj.putDeepField(
          Arrays.copyOfRange(keys, 1, keys.length),
          Arrays.copyOfRange(isJsonObj, 1, keys.length),
          value,
          orderIndex);
    } else { // Object
      ValidMsgList obj;
      if (this.has(key)) {
        obj = (ValidMsgList) this.get(key);
      } else {
        obj = new ValidMsgList();
        this.put(key, obj);
      }
      obj.putDeepField(
          Arrays.copyOfRange(keys, 1, keys.length),
          Arrays.copyOfRange(isJsonObj, 1, keys.length),
          value,
          orderIndex);
    }
  }

  public void putDeepField(String name, String message) {
    putDeepField(name, message, -1);
  }

  public void putDeepField(String name, String message, Integer orderIndex) {
    this.putDeepField(name, message, orderIndex, ValidateType.NORMAL);
  }

  /**
   * @param name String
   * @param message String
   * @param orderIndex Integer
   */
  public void putDeepField(
      String name, String message, Integer orderIndex, ValidateType validateType) {
    String indexPattern = "([^\\[]*)\\[([0-9]*)\\].([^\\[]*)";
    Pattern r = Pattern.compile(indexPattern);
    Matcher m = r.matcher(name);
    m.matches();
    // Special case: validate error
    try {
      if (!m.group(1).isEmpty() && !m.group(2).isEmpty() && !m.group(3).isEmpty()) {
        String key = m.group(1);
        Integer index = Integer.parseInt(m.group(2));
        String subKey = m.group(3);
        this.putDeepField(
            new String[] {key, index.toString(), subKey},
            new boolean[] {false, true, false},
            message,
            orderIndex);
      }
      return;
    } catch (Exception e) {
      // log.warn(e.getMessage());
    }

    // check list in list case
    indexPattern = "([^\\[]*)\\[([0-9]*)\\].([^\\[]*)\\[([0-9]*)\\]";
    r = Pattern.compile(indexPattern);
    m = r.matcher(name);
    m.matches();
    try {
      if (!m.group(1).isEmpty()
          && !m.group(2).isEmpty()
          && !m.group(3).isEmpty()
          && !m.group(4).isEmpty()) {
        String key1 = m.group(1);
        Integer index1 = Integer.parseInt(m.group(2));
        String key2 = m.group(3);
        Integer index2 = Integer.parseInt(m.group(4));
        this.putDeepField(
            new String[] {key1, index1.toString(), key2, index2.toString()},
            new boolean[] {false, true, false, true},
            message,
            orderIndex);
      }
      return;
    } catch (Exception e) {
      // log.warn(e.getMessage());
    }

    // Normal case: validate error
    JSONArray arr;
    if (validateType == ValidateType.GLOBALOBJINOBJ
        || validateType == ValidateType.PRIVATEOBJINOBJ) {
      String indexPrivateObjInObjPattern = "([^\\.]*).([^\\.]*)";
      r = Pattern.compile(indexPrivateObjInObjPattern);
      m = r.matcher(name);
      m.matches();
      try {
        if (!m.group(1).isEmpty() && !m.group(2).isEmpty()) {
          String key = m.group(1);
          String subKey = m.group(2);
          this.putDeepField(
              new String[] {key, subKey}, new boolean[] {false, false}, message, orderIndex);
        }
        return;
      } catch (Exception e) {
        // log.warn(e.getMessage());
      }
    }
    if (this.has(name)) {
      arr = (JSONArray) this.get(name);
    } else {
      arr = new JSONArray();
    }
    // arr.put(message);
    putMessage(arr, message, orderIndex);
    this.put(name, arr);
  }

  public Map<String, Object> getMap() {
    this.filterAllLeaf();
    return this.toMap();
  }

  private void filterAllLeaf() {
    Iterator<String> keys = this.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      Object obj = this.get(key);
      if (obj instanceof ValidMsgList) {
        ((ValidMsgList) obj).filterAllLeaf();
      } else if (obj instanceof JSONArray) {
        JSONArray arr = (JSONArray) obj;
        List<Pair<Integer, String>> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); ++i) {
          Object el = arr.get(i);
          if (el instanceof ValidMsgList) {
            ((ValidMsgList) el).filterAllLeaf();
          } else if (el instanceof Pair) {
            Pair<Integer, String> leaf = (Pair<Integer, String>) el;
            list.add(leaf);
          }
        }
        if (list.size() > 0) {
          list.sort(Comparator.comparing(Pair::getFirst));
          JSONArray errorList = new JSONArray();
          for (int i = 0; i < list.size(); ++i) {
            errorList.put(list.get(i).getSecond());
          }
          this.put(key, errorList);
        }
      }
    }
  }
}

