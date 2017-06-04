
package com.pairtodopremium.data.response.invite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pairtodopremium.data.response.signup.Error;
import com.pairtodopremium.data.response.signup.Result;

public class InviteResponse {

    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("result")
    @Expose
    private Object result;
    @SerializedName("version")
    @Expose
    private Integer version;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}