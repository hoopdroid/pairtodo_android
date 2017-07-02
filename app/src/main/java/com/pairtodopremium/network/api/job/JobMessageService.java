package com.pairtodopremium.network.api.job;

import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;

public class JobMessageService {
  public static final int JOB_MESSAGE_ACTIVE_ID = 1;
  public static final int JOB_MESSAGE_BACKGROUND_ID = 2;
  public static final int JOB_PULSE_BACKGROUND_ID = 3;
  public static final int JOB_PULSE_ACTIVE_ID = 4;
  public static final int MESSAGE_SERVICE_ACTIVE_INTERVAL = 1000;
  public static final int MESSAGE_SERVICE_BACKGROUND_INTERVAL = 10000;
  public static final int JOB_SERVICE_BACKGROUND_INTERVAL = 10000;
  private static final JobMessageService ourInstance = new JobMessageService();
  private static final String JOB_PERIODIC_TASK_TAG =
      "io.hypertrack.android_scheduler_demo.JobPeriodicTask";

  private JobMessageService() {
  }

  public static JobMessageService getInstance() {
    return ourInstance;
  }

  public Job getNewMessages(SmartScheduler.JobScheduledCallback callback) {
    Long intervalInMillis = (long) (MESSAGE_SERVICE_ACTIVE_INTERVAL);

    Job.Builder builder =
        new Job.Builder(JOB_MESSAGE_ACTIVE_ID, callback, Job.Type.JOB_TYPE_HANDLER,
            "").setRequiredNetworkType(Job.NetworkType.NETWORK_TYPE_ANY)
            .setIntervalMillis(intervalInMillis)
            .setPeriodic(MESSAGE_SERVICE_ACTIVE_INTERVAL);

    return builder.build();
  }

  public Job checkCoupleStatus(SmartScheduler.JobScheduledCallback callback) {
    Long intervalInMillis = (long) (MESSAGE_SERVICE_ACTIVE_INTERVAL);

    Job.Builder builder = new Job.Builder(JOB_PULSE_ACTIVE_ID, callback, Job.Type.JOB_TYPE_HANDLER,
        "").setRequiredNetworkType(Job.NetworkType.NETWORK_TYPE_ANY)
        .setIntervalMillis(intervalInMillis)
        .setPeriodic(MESSAGE_SERVICE_ACTIVE_INTERVAL);

    return builder.build();
  }

  public Job getNewBackgroundMessages(SmartScheduler.JobScheduledCallback callback) {
    Long intervalInMillis = (long) (MESSAGE_SERVICE_ACTIVE_INTERVAL);

    Job.Builder builder =
        new Job.Builder(JOB_MESSAGE_BACKGROUND_ID, callback, Job.Type.JOB_TYPE_PERIODIC_TASK,
            JOB_PERIODIC_TASK_TAG).setRequiredNetworkType(Job.NetworkType.NETWORK_TYPE_ANY)
            .setIntervalMillis(intervalInMillis)
            .setPeriodic(MESSAGE_SERVICE_BACKGROUND_INTERVAL);

    return builder.build();
  }

  public Job getUserPulse(SmartScheduler.JobScheduledCallback callback) {
    Long intervalInMillis = (long) (MESSAGE_SERVICE_ACTIVE_INTERVAL);

    Job.Builder builder =
        new Job.Builder(JOB_PULSE_BACKGROUND_ID, callback, Job.Type.JOB_TYPE_PERIODIC_TASK,
            JOB_PERIODIC_TASK_TAG).setRequiredNetworkType(Job.NetworkType.NETWORK_TYPE_ANY)
            .setIntervalMillis(intervalInMillis)
            .setPeriodic(JOB_SERVICE_BACKGROUND_INTERVAL);

    return builder.build();
  }
}
