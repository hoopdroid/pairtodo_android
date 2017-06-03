package com.pairtodobeta.data.response.userData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserInfo extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nic_name")
    @Expose
    private String nicName;
    @SerializedName("registred")
    @Expose
    private String registred;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("tel")
    @Expose
    private String tel;
    @SerializedName("pair_id")
    @Expose
    private String pairId;
    @SerializedName("pair_name")
    @Expose
    private String pairName;
    @SerializedName("last_modify")
    @Expose
    private String lastModify;
    @SerializedName("photo_real")
    @Expose
    private String photoReal;
    @SerializedName("bg_thema")
    @Expose
    private String bgThema;
    @SerializedName("is_vip")
    @Expose
    private String isVip;
    @SerializedName("vip_date")
    @Expose
    private String vipDate;
    @SerializedName("pair_photo")
    @Expose
    private String pairPhoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public String getRegistred() {
        return registred;
    }

    public void setRegistred(String registred) {
        this.registred = registred;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public String getPairId() {
        return pairId;
    }

    public void setPairId(String pairId) {
        this.pairId = pairId;
    }

    public String getPairName() {
        return pairName;
    }

    public void setPairName(String pairName) {
        this.pairName = pairName;
    }


    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }


    public String getPhotoReal() {
        return photoReal;
    }

    public void setPhotoReal(String photoReal) {
        this.photoReal = photoReal;
    }

    public String getBgThema() {
        return bgThema;
    }

    public void setBgThema(String bgThema) {
        this.bgThema = bgThema;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getVipDate() {
        return vipDate;
    }

    public void setVipDate(String vipDate) {
        this.vipDate = vipDate;
    }

    public String getPairPhoto() {
        return pairPhoto;
    }

    public void setPairPhoto(String pairPhoto) {
        this.pairPhoto = pairPhoto;
    }


}
