package com.pairtodopremium.data.response.stats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatsData {

  @SerializedName("error") @Expose private Error error;
  @SerializedName("result") @Expose private Result result;
  @SerializedName("version") @Expose private Integer version;

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