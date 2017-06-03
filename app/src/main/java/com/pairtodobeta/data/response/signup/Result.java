
package com.pairtodobeta.data.response.signup;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("user_id")
    public String userId;
    @SerializedName("token")
    public String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}