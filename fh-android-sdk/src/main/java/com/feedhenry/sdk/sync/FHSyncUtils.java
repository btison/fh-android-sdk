package com.feedhenry.sdk.sync;

import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONWriter;

public class FHSyncUtils {

  private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
      '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static JSONArray sortObj(Object pObject) throws Exception {
    JSONArray results = new JSONArray();
    if (pObject instanceof JSONArray) {
      JSONArray castedObj = (JSONArray) pObject;
      for (int i = 0; i < castedObj.length(); i++) {
        FHJSONObjectWrapper obj = new FHJSONObjectWrapper(i+"", castedObj.get(i));
        results.put(obj);
      }
    } else if (pObject instanceof JSONObject) {
      JSONArray keys = ((JSONObject) pObject).names();
      List<String> sortedKeys = sortNames(keys);
      for (int i = 0; i < sortedKeys.size(); i++) {
        String key = sortedKeys.get(i);
        FHJSONObjectWrapper obj = new FHJSONObjectWrapper(key, ((JSONObject) pObject).get(key));
        results.put(obj);
      }
    } else {
      throw new Exception("object is not JSONObject or JSONArray");
    }
    return results;
  }

  public static String generateObjectHash(Object pObject) {
    String hashValue = "";
    try {
      JSONArray sorted = sortObj(pObject);
      String s = sorted.toString();
      hashValue = generateHash(sorted.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return hashValue;
  }

  public static String generateHash(String pText) throws Exception {
    String hashValue = null;
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.reset();
    md.update(pText.getBytes("ASCII"));
    hashValue = encodeHex(md.digest());
    return hashValue;
  }

  private static String encodeHex(byte[] pData) {
    int l = pData.length;

    char[] out = new char[l << 1];

    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++) {
      out[j++] = DIGITS[(0xF0 & pData[i]) >>> 4];
      out[j++] = DIGITS[0x0F & pData[i]];
    }

    return new String(out);
  }

  private static List<String> sortNames(JSONArray pNames) throws JSONException {
    ArrayList<String> names = new ArrayList<String>();
    if (null != pNames) {
      for (int i = 0; i < pNames.length(); i++) {
        names.add(pNames.getString(i));
      }
      Collections.sort(names);
    }
    return names;
  }

  public static class FHJSONObjectWrapper implements JSONString {
    private String mKey;
    private Object mValue;

    public FHJSONObjectWrapper(String pKey, Object pValue){
      this.key(pKey);
      this.value(pValue);
    }

    public void key(String pKey){
      this.mKey = pKey;
    }

    public void value(Object pValue){
      if(pValue instanceof JSONObject || pValue instanceof  JSONArray){
        try {
          this.mValue = sortObj(pValue);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        this.mValue = pValue;
      }
    }

    @Override public String toJSONString() {
      StringWriter sw = new StringWriter();
      JSONWriter jw = new JSONWriter(sw);
      jw.object().key("key").value(this.mKey).key("value").value(this.mValue).endObject();
      String s = sw.toString();
      System.out.println("sorted toJSONString = " + s);
      return s;
    }

    @Override public String toString() {
      StringWriter sw = new StringWriter();
      JSONWriter jw = new JSONWriter(sw);
      jw.object().key("key").value(this.mKey).key("value").value(this.mValue).endObject();
      String s = sw.toString();
      System.out.println("sorted toString = " + s);
      return s;
    }
  }
}
