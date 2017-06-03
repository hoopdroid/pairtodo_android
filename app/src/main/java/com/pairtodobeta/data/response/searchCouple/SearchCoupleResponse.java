package com.pairtodobeta.data.response.searchCouple;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class SearchCoupleResponse {

    public static TypeAdapter<SearchCoupleResponse> typeAdapter(Gson gson) {
        return new AutoValue_SearchCoupleResponse.GsonTypeAdapter(gson);
    }

    @SerializedName("result")
    public abstract List<User> result();

    @SerializedName("error")
    public abstract Error error();

    @SerializedName("version")
    public abstract int version();
}