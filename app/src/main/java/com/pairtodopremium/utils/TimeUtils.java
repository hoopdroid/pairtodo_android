package com.pairtodopremium.utils;

public class TimeUtils {

  public static long getTimeStamp() {
    long timestamp = System.currentTimeMillis() / 1000L;
    return timestamp;
  }
}
