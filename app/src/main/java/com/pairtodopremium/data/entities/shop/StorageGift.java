package com.pairtodopremium.data.entities.shop;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StorageGift implements Parcelable {

  public static final Parcelable.Creator<StorageGift> CREATOR =
      new Parcelable.Creator<StorageGift>() {
        @Override public StorageGift createFromParcel(Parcel source) {
          return new StorageGift(source);
        }

        @Override public StorageGift[] newArray(int size) {
          return new StorageGift[size];
        }
      };
  @SerializedName("nom") @Expose private Integer nom;
  @SerializedName("name_ru") @Expose private String nameRu;
  @SerializedName("name_en") @Expose private String nameEn;
  @SerializedName("tovarName") @Expose private String tovarName;
  @SerializedName("price") @Expose private String price;
  @SerializedName("preview") @Expose private String preview;
  @SerializedName("full") @Expose private String full;

  public StorageGift() {
  }

  protected StorageGift(Parcel in) {
    this.nom = (Integer) in.readValue(Integer.class.getClassLoader());
    this.nameRu = in.readString();
    this.nameEn = in.readString();
    this.tovarName = in.readString();
    this.price = in.readString();
    this.preview = in.readString();
    this.full = in.readString();
  }

  public Integer getNom() {
    return nom;
  }

  public void setNom(Integer nom) {
    this.nom = nom;
  }

  public String getNameRu() {
    return nameRu;
  }

  public void setNameRu(String nameRu) {
    this.nameRu = nameRu;
  }

  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  public String getTovarName() {
    return tovarName;
  }

  public void setTovarName(String tovarName) {
    this.tovarName = tovarName;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getPreview() {
    return preview;
  }

  public void setPreview(String preview) {
    this.preview = preview;
  }

  public String getFull() {
    return full;
  }

  public void setFull(String full) {
    this.full = full;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.nom);
    dest.writeString(this.nameRu);
    dest.writeString(this.nameEn);
    dest.writeString(this.tovarName);
    dest.writeString(this.price);
    dest.writeString(this.preview);
    dest.writeString(this.full);
  }
}