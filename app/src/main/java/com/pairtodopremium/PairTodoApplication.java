package com.pairtodopremium;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;
import com.facebook.stetho.Stetho;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.chat.Message;
import com.pairtodopremium.data.response.pulse.Pulse;
import com.pairtodopremium.db.PairToDoCacheDao;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.event.GIftEvent;
import com.pairtodopremium.event.JobEvent;
import com.pairtodopremium.event.JobMessageEvent;
import com.pairtodopremium.event.MessageEvent;
import com.pairtodopremium.event.PairAddEvent;
import com.pairtodopremium.network.api.job.JobMessageService;
import com.pairtodopremium.ui.main.MainActivity;
import com.pairtodopremium.ui.main.chat.ChatActivity;
import com.pairtodopremium.ui.main.profile.gifts.GiftsActivity;
import com.pairtodopremium.utils.Constants;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class PairTodoApplication extends Application {
  private static PairToDoCacheDao cacheManager;
  private VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
    @Override public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
      if (newToken == null) {
        Intent intent = new Intent(PairTodoApplication.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    }
  };

  public static void checkPulseInBackground(final Context context, final String token) {
    android.os.Handler handler = new android.os.Handler();
    handler.post(new Runnable() {
      @Override public void run() {
        SmartScheduler jobScheduler = SmartScheduler.getInstance(context);
        Job job = JobMessageService.getInstance().
            getUserPulse(new SmartScheduler.JobScheduledCallback() {
              @Override public void onJobScheduled(final Context context, final Job job) {
                if (job != null) {
                  Log.d("GETTING PULSE", "FROM BACKGROUND with 30 sec");

                  DataService.init().getUserPulse(new DataService.onGetPulseTask() {
                    @Override public void onGetPulseResult(Pulse pulse) {
                      if (pulse.message().equals("1")) {
                        DataService.init().getNewMessages(new DataService.onMessageGetTask() {
                          @Override public void onMessageResult(List<Message> messages) {
                            if (messages.size() > 0) {
                              if (!messages.get(messages.size() - 1).fromId().
                                  equals(PrefRepository.getUserId(context))) {
                                if (!messages.get(messages.size() - 1)
                                    .getText()
                                    .contains(Constants.STICKER)) {
                                  showNotification(context,
                                      context.getString(R.string.new_message_from_couple),
                                      messages.get(messages.size() - 1).getText(),
                                      ChatActivity.class);
                                  EventBus.getDefault()
                                      .post(new MessageEvent(
                                          messages.get(messages.size() - 1).getText()));
                                } else {
                                  showNotification(context,
                                      context.getString(R.string.new_message_from_couple),
                                      context.getString(R.string.sticker), ChatActivity.class);
                                  EventBus.getDefault().post(new MessageEvent(""));
                                }
                                cacheManager.addMessagesToCache(messages);
                              }
                            }
                          }

                          @Override public void onMessageError(String message) {

                          }
                        }, PrefRepository.getToken(context));
                      }
                      if (pulse.job().equals("1")) {
                        showNotification(context, context.getString(R.string.app_name),
                            context.getString(R.string.you_have_changes), MainActivity.class);
                        EventBus.getDefault().post(new JobEvent());
                      }
                      if (pulse.mail().equals("1")) {
                        showNotification(context, context.getString(R.string.app_name),
                            context.getString(R.string.you_got_gift), GiftsActivity.class);
                        EventBus.getDefault().post(new GIftEvent());
                      }
                      if (pulse.pair().equals("1") || pulse.pairAdd().equals("1")) {
                        showNotification(context, context.getString(R.string.app_name),
                            context.getString(R.string.couple_joined), MainActivity.class);
                        EventBus.getDefault().post(new PairAddEvent());
                      }

                      if (pulse.jobMessage().equals("1")) {
                        showNotification(context, context.getString(R.string.app_name),
                            context.getString(R.string.new_message_from_couple),
                            MainActivity.class);
                        EventBus.getDefault().post(new JobMessageEvent());
                      }

                      Log.d("USER PULSE", pulse.toString());
                    }

                    @Override public void onGetPulseError(String error) {
                      Log.d("USER PULSE ERROR", "");
                    }
                  }, token);
                }
              }
            });
        jobScheduler.addJob(job);
      }
    });
  }

  public static void checkNewMessagesInBackground(final Context context, final String token) {
    android.os.Handler handler = new android.os.Handler();
    handler.post(new Runnable() {
      @Override public void run() {
        SmartScheduler jobScheduler = SmartScheduler.getInstance(context);
        Job job = JobMessageService.getInstance().
            getNewBackgroundMessages(new SmartScheduler.JobScheduledCallback() {
              @Override public void onJobScheduled(final Context context, final Job job) {
                if (job != null) {
                  Log.d("GETTING MESSAGES", "FROM BACKGROUND with 10 sec");
                  DataService.init().getNewMessages(new DataService.onMessageGetTask() {
                    @Override public void onMessageResult(List<Message> messages) {
                      if (messages.size() > 0) {
                        showNotification(context, "Новое сообщение",
                            messages.get(messages.size() - 1).getText(), ChatActivity.class);
                      }
                    }

                    @Override public void onMessageError(String message) {
                      Log.d("error retrofit", message);
                      int a = 5;
                    }
                  }, token);
                }
              }
            });
        jobScheduler.addJob(job);
      }
    });
  }

  public static void showNotification(Context context, String title, String message, Class cls) {
    Intent notificationIntent = new Intent(context, cls);
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
        PendingIntent.FLAG_CANCEL_CURRENT);

    Notification.Builder builder =
        new Notification.Builder(context).setSmallIcon(R.drawable.ic_push)
            .setColor(R.color.colorAccent)
            .setContentIntent(contentIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)
            .setLargeIcon(
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
    Notification notification = builder.build();

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.notify(1, notification);
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override public void onCreate() {
    super.onCreate();

    cacheManager = new PairToDoCacheDao();
    Realm.init(this);
    RealmConfiguration config =
        new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
    Realm.setDefaultConfiguration(config);

    vkAccessTokenTracker.startTracking();
    VKSdk.initialize(this);

    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    String token = settings.getString("userToken", "");
    if (!token.equals("")) checkPulseInBackground(getApplicationContext(), token);

    Stetho.initialize(Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
        .build());

    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/RobotoSlab-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build());
  }

  public void removeBackgroundJob() {

    SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
    if (!jobScheduler.contains(JobMessageService.JOB_MESSAGE_ACTIVE_ID)) {
      Toast.makeText(this, "No job exists with JobID: " + JobMessageService.JOB_MESSAGE_ACTIVE_ID,
          Toast.LENGTH_SHORT).show();
      return;
    }

    if (jobScheduler.removeJob(JobMessageService.JOB_MESSAGE_ACTIVE_ID)) {
      Toast.makeText(this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
    }
  }
}
