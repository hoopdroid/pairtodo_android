package com.pairtodobeta.ui.main.chat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StickersItem {

    @SerializedName("preview")
    @Expose
    private String preview;

    @SerializedName("col")
    @Expose
    private int col;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("tovarName")
    @Expose
    private String tovarName;

    @SerializedName("name_ru")
    @Expose
    private String nameRu;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("sticker")
    @Expose
    private String sticker;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("row")
    @Expose
    private int row;

    @SerializedName("nom")
    @Expose
    private int nom;

    @SerializedName("name_en")
    @Expose
    private String nameEn;

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTovarName() {
        return tovarName;
    }

    public void setTovarName(String tovarName) {
        this.tovarName = tovarName;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNom() {
        return nom;
    }

    public void setNom(int nom) {
        this.nom = nom;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public String toString() {
        return
                "StickersItem{" +
                        "preview = '" + preview + '\'' +
                        ",col = '" + col + '\'' +
                        ",path = '" + path + '\'' +
                        ",tovarName = '" + tovarName + '\'' +
                        ",name_ru = '" + nameRu + '\'' +
                        ",price = '" + price + '\'' +
                        ",sticker = '" + sticker + '\'' +
                        ",count = '" + count + '\'' +
                        ",row = '" + row + '\'' +
                        ",nom = '" + nom + '\'' +
                        ",name_en = '" + nameEn + '\'' +
                        "}";
    }
}