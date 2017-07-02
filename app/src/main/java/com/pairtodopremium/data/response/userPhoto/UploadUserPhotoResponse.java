package com.pairtodopremium.data.response.userPhoto;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue public abstract class UploadUserPhotoResponse {

  public static TypeAdapter<UploadUserPhotoResponse> typeAdapter(Gson gson) {
    return new AutoValue_UploadUserPhotoResponse.GsonTypeAdapter(gson);
  }

  @SerializedName("result") public abstract UserImage userImage();
}