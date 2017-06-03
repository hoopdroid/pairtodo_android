package com.pairtodobeta.ui.main.chat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StickersData {

    @SerializedName("stickers")
    @Expose
    private List<StickersItem> stickers;

    public List<StickersItem> getStickers() {
        return stickers;
    }

    public void setStickers(List<StickersItem> stickers) {
        this.stickers = stickers;
    }

    @Override
    public String toString() {
        return
                "StickersData{" +
                        "stickers = '" + stickers + '\'' +
                        "}";
    }
}