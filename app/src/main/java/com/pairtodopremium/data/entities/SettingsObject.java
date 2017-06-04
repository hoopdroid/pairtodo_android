package com.pairtodopremium.data.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class SettingsObject extends RealmObject {

    @PrimaryKey
    private int userId;
    private String userToken;
    private String userPhone;

    public SettingsObject() {
    }

    public SettingsObject(int userId, String userToken, String userPhone) {
        this.userId = userId;
        this.userToken = userToken;
        this.userPhone = userPhone;
    }

    public SettingsObject(int userId, String userToken) {
        this.userId = userId;
        this.userToken = userToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}