package com.pairtodopremium.data.response.userPhoto;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;

@AutoValue
public abstract class UploadUserPhotoResponse{

	@SerializedName("result")
	public abstract UserImage userImage();

	public static TypeAdapter<UploadUserPhotoResponse> typeAdapter(Gson gson) {
		return new AutoValue_UploadUserPhotoResponse.GsonTypeAdapter(gson);
	}
}