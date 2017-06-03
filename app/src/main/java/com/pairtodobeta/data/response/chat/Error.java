package com.pairtodobeta.data.response.chat;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Error{

	@SerializedName("code")
	public abstract String code();

	public static TypeAdapter<Error> typeAdapter(Gson gson) {
		return new AutoValue_Error.GsonTypeAdapter(gson);
	}
}