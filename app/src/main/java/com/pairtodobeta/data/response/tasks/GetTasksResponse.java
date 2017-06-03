package com.pairtodobeta.data.response.tasks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pairtodobeta.data.entities.Task;

import java.util.List;

public class GetTasksResponse {

    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("result")
    @Expose
    private List<Task> result;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("version")
    @Expose
    private Integer version;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<Task> getResult() {
        return result;
    }

    public void setResult(List<Task> result) {
        this.result = result;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}