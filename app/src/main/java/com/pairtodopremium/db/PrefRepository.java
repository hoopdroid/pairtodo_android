package com.pairtodopremium.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.pairtodopremium.utils.Themes;

public class PrefRepository {

  public static void setIsAuthorized(Context activity) {
    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(activity).edit();
    prefs.putInt("isAuthorized", 1);
    prefs.apply();
  }

  public static void setLastMessage(Context context, String messageId) {
    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
    prefs.putString("lastMessage", messageId);
    prefs.apply();
  }

  public static void setUserId(Context context, String id) {
    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
    prefs.putString("userId", id);
    prefs.apply();
  }

  public static void setToken(Context activity, String token) {
    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(activity).edit();
    prefs.putString("userToken", token);
    prefs.apply();
  }

  public static String getToken(Context activity) {
    return PreferenceManager.getDefaultSharedPreferences(activity).getString("userToken", "");
  }

  public static String getLastMessageId(Context activity) {
    return PreferenceManager.getDefaultSharedPreferences(activity).getString("lastMessage", "");
  }

  public static String getUserId(Context activity) {
    return PreferenceManager.getDefaultSharedPreferences(activity).getString("userId", "");
  }

  public static void setTheme(Context activity, String theme) {
    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(activity).edit();
    prefs.putString("theme", theme);
    prefs.apply();
  }

  public static String getTheme(Context context) {
    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    return settings.getString("theme", Themes.THEME_DEFAULT);
  }

  public static void setCouplePhone(Context activity, String pairWaitingNumber) {
    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(activity).edit();
    prefs.putString("couplePhone", pairWaitingNumber);
    prefs.apply();
  }

  public static String getCouplePhone(Context context) {
    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    return settings.getString("couplePhone", "");
  }

  public static void setMyPhone(Context activity, String pairWaitingNumber) {
    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(activity).edit();
    prefs.putString("myPhone", pairWaitingNumber);
    prefs.apply();
  }

  public static String getMyPhone(Context context) {
    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    return settings.getString("myPhone", "");
  }
}
