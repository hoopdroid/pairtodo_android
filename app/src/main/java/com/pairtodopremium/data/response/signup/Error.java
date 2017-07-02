package com.pairtodopremium.data.response.signup;

import com.google.gson.annotations.SerializedName;

public class Error {

  @SerializedName("code") public String code;
  @SerializedName("message") public String message;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
