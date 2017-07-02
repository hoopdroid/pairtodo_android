package com.pairtodopremium.data.response.userPhoto;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue public abstract class UserImage {

  public static TypeAdapter<UserImage> typeAdapter(Gson gson) {
    return new AutoValue_UserImage.GsonTypeAdapter(gson);
  }

  @SerializedName("photo") public abstract String photo();
}