package com.pairtodobeta.data.response.searchCouple;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;

@AutoValue
public abstract class User {

	@SerializedName("sex")
	@Nullable
	public abstract String sex();

	@SerializedName("name")
	public abstract String name();

	@SerializedName("photo")
	public abstract String photo();

	@SerializedName("id")
	public abstract String id();

	@SerializedName("email")
	public abstract String email();

	@SerializedName("nic_name")
	public abstract String nicName();

	public static TypeAdapter<User> typeAdapter(Gson gson) {
		return new AutoValue_User.GsonTypeAdapter(gson);
	}
}