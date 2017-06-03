package com.pairtodobeta.data.response.userPhoto;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;

@AutoValue
public abstract class UserImage {

	@SerializedName("photo")
	public abstract String photo();

	public static TypeAdapter<UserImage> typeAdapter(Gson gson) {
		return new AutoValue_UserImage.GsonTypeAdapter(gson);
	}
}