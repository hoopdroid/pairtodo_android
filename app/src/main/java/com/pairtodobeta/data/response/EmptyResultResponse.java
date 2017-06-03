package com.pairtodobeta.data.response;

import com.google.gson.annotations.SerializedName;
import com.pairtodobeta.data.response.signup.Error;
import com.pairtodobeta.data.response.signup.Result;

public class EmptyResultResponse {

    @SerializedName("error")
    public Error error;
    @SerializedName("result")
    public Object result;

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


}