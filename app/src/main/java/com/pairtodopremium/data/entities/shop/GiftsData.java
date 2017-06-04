package com.pairtodopremium.data.entities.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftsData {

    @SerializedName("gifts")
    @Expose
    private List<StorageGift> storageGifts = null;

    public List<StorageGift> getStorageGifts() {
        return storageGifts;
    }

    public void setStorageGifts(List<StorageGift> storageGifts) {
        this.storageGifts = storageGifts;
    }

}