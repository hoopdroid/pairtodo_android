package com.pairtodopremium.data.response.tasks;

import com.google.gson.annotations.SerializedName;
import com.pairtodopremium.data.response.signup.Error;
import com.pairtodopremium.data.response.signup.Result;

public class ChangeTaskResponse {

  @SerializedName("error") public com.pairtodopremium.data.response.signup.Error error;
  @SerializedName("result") public Object result;
  @SerializedName("version") public Integer version;

  public com.pairtodopremium.data.response.signup.Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
  }

  public Object getResult() {
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