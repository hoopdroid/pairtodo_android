
package com.pairtodobeta.data.response.userData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserDataResponse {

    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("result")
    @Expose
    private UserInfo userInfo;
    @SerializedName("version")
    @Expose
    private Integer version;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
