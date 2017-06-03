package com.pairtodobeta.data.response.chat;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class ChatDataResponse{

	@SerializedName("result")
	@Nullable
	public abstract List<Message> result();

	@SerializedName("error")
	@Nullable
	public abstract Error error();


	public static TypeAdapter<ChatDataResponse> typeAdapter(Gson gson) {
		return new AutoValue_ChatDataResponse.GsonTypeAdapter(gson);
	}
}