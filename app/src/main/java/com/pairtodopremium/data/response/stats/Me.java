package com.pairtodopremium.data.response.stats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Me {

  @SerializedName("add_job") @Expose private String addJob;
  @SerializedName("del_job") @Expose private String delJob;
  @SerializedName("assign_job") @Expose private String assignJob;
  @SerializedName("obtained_job") @Expose private String obtainedJob;
  @SerializedName("delay_job") @Expose private String delayJob;
  @SerializedName("during_job") @Expose private String duringJob;
  @SerializedName("executed_job") @Expose private String executedJob;
  @SerializedName("renew_job") @Expose private String renewJob;

  public String getAddJob() {
    return addJob;
  }

  public void setAddJob(String addJob) {
    this.addJob = addJob;
  }

  public String getDelJob() {
    return delJob;
  }

  public void setDelJob(String delJob) {
    this.delJob = delJob;
  }

  public String getAssignJob() {
    return assignJob;
  }

  public void setAssignJob(String assignJob) {
    this.assignJob = assignJob;
  }

  public String getObtainedJob() {
    return obtainedJob;
  }

  public void setObtainedJob(String obtainedJob) {
    this.obtainedJob = obtainedJob;
  }

  public String getDelayJob() {
    return delayJob;
  }

  public void setDelayJob(String delayJob) {
    this.delayJob = delayJob;
  }

  public String getDuringJob() {
    return duringJob;
  }

  public void setDuringJob(String duringJob) {
    this.duringJob = duringJob;
  }

  public String getExecutedJob() {
    return executedJob;
  }

  public void setExecutedJob(String executedJob) {
    this.executedJob = executedJob;
  }

  public String getRenewJob() {
    return renewJob;
  }

  public void setRenewJob(String renewJob) {
    this.renewJob = renewJob;
  }
}