package com.pairtodopremium.data.response.pulse;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue public abstract class Pulse {

  public static TypeAdapter<Pulse> typeAdapter(Gson gson) {
    return new AutoValue_Pulse.GsonTypeAdapter(gson);
  }

  @SerializedName("job_message") public abstract String jobMessage();

  @SerializedName("system_message") public abstract String systemMessage();

  @SerializedName("shop") public abstract String shop();

  @SerializedName("mail") public abstract String mail();

  @SerializedName("profile") public abstract String profile();

  @SerializedName("pair_last_visit") public abstract String pairLastVisit();

  @SerializedName("job") public abstract String job();

  @SerializedName("message") public abstract String message();

  @SerializedName("pair_add") public abstract String pairAdd();

  @SerializedName("pair") public abstract String pair();
}