package com.pairtodopremium.data.response.gifts;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class GiftsResponse{

	@SerializedName("result")
	public abstract List<Gift> result();;

	public static TypeAdapter<GiftsResponse> typeAdapter(Gson gson) {
		return new AutoValue_GiftsResponse.GsonTypeAdapter(gson);
	}
}