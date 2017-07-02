package com.pairtodopremium.data.response.gifts;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue public abstract class Gift {

  public static TypeAdapter<Gift> typeAdapter(Gson gson) {
    return new AutoValue_Gift.GsonTypeAdapter(gson);
  }

  @SerializedName("template") public abstract String template();

  @SerializedName("is_read") public abstract String isRead();

  @SerializedName("template_name") public abstract String templateName();

  @SerializedName("from_id") public abstract String fromId();

  @SerializedName("to_id") public abstract String toId();

  @SerializedName("id") public abstract String id();

  @SerializedName("text") public abstract String text();

  @SerializedName("create_date") public abstract String createDate();
}