package com.pairtodopremium.utils;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

public class JsonHelper {

  public static String loadJSONFromAsset(Context activity, String jsonName) {
    String json = null;
    try {
      InputStream is = activity.getAssets().open(jsonName + ".json");
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, "UTF-8");
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return json;
  }
}
