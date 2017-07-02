package com.pairtodopremium.data.response.tasks;

import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.pairtodopremium.data.response.signup.Error;

public class UploadImageData {

  @SerializedName("error") public Error error;
  @SerializedName("result") @Nullable public UploadPhotoResponse result;
  @SerializedName("version") public Integer version;
  @SerializedName("info") public String info;

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
  }

  public UploadPhotoResponse getResult() {
    return result;
  }

  public void setResult(UploadPhotoResponse result) {
    this.result = result;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}