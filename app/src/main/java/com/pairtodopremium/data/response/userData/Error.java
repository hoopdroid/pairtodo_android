
package com.pairtodopremium.data.response.userData;

import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("code")
    public String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
