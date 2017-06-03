package com.pairtodobeta.data.response.tasks;

import com.google.gson.annotations.SerializedName;
import com.pairtodobeta.data.response.signup.Error;
import com.pairtodobeta.data.response.signup.Result;

public class ChangeTaskResponse {

    @SerializedName("error")
    public com.pairtodobeta.data.response.signup.Error error;
    @SerializedName("result")
    public Object result;
    @SerializedName("version")
    public Integer version;

    public com.pairtodobeta.data.response.signup.Error getError() {
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