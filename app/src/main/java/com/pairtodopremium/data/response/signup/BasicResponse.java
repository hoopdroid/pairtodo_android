package com.pairtodopremium.data.response.signup;

import com.google.gson.annotations.SerializedName;

public class BasicResponse {

  @SerializedName("error") public Error error;
  @SerializedName("result") public Result result;
  @SerializedName("version") public Integer version;

  public Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
  }

  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}