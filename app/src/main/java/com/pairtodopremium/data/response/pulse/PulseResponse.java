package com.pairtodopremium.data.response.pulse;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue public abstract class PulseResponse {

  public static TypeAdapter<PulseResponse> typeAdapter(Gson gson) {
    return new AutoValue_PulseResponse.GsonTypeAdapter(gson);
  }

  @SerializedName("result") public abstract Pulse result();

  @SerializedName("error") public abstract Error error();
}