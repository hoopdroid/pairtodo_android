package com.pairtodopremium.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.pairtodopremium.PairTodoApplication;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.pulse.Pulse;
import com.pairtodopremium.data.response.userData.UserDataResponse;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PairToDoCacheDao;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.event.PairAddEvent;
import com.pairtodopremium.network.api.job.JobMessageService;
import com.pairtodopremium.utils.Themes;
import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;
import io.realm.Realm;
import java.io.File;
import org.greenrobot.eventbus.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

  public static Realm realm;
  protected PairToDoCacheDao cacheManager = new PairToDoCacheDao();

  public static boolean deleteFile(File file) {
    boolean deletedAll = true;

    if (file != null) {
      if (file.isDirectory()) {
        String[] children = file.list();
        for (int i = 0; i < children.length; i++) {
          deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
        }
      } else {
        deletedAll = file.delete();
      }
    }

    return deletedAll;
  }

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    PairTodoApplication.checkPulseInBackground(getApplicationContext(),
        PrefRepository.getToken(getApplicationContext()));
  }

  @Override public void setContentView(@LayoutRes int layoutResID) {
    chooseUserTheme();

    super.setContentView(layoutResID);
    ButterKnife.bind(this);
    realm = Realm.getDefaultInstance();
  }

  public boolean isNetworkAvailable() {
    ConnectivityManager connectivityMgr =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
    /// if no network is available networkInfo will be null
    if (networkInfo != null && networkInfo.isConnected()) {
      return true;
    }
    return false;
  }

  public void checkCoupleStatus() {
    Handler handler = new Handler();
    final SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
    handler.post(new Runnable() {
      @Override public void run() {
        Job job = JobMessageService.getInstance()
            .checkCoupleStatus(new SmartScheduler.JobScheduledCallback() {
              @Override public void onJobScheduled(final Context context, Job job) {
                if (job != null) {
                  Log.d("GETTING COUPLE STATUS", "FROM ACTIVE with 1 sec");

                  DataService.init().getUserPulse(new DataService.onGetPulseTask() {
                    @Override public void onGetPulseResult(Pulse pulse) {
                      if (pulse.pair().equals("1") || pulse.pairAdd().equals("1")) {
                        Toast.makeText(context, R.string.couple_joined, Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new PairAddEvent());
                        removeCheckStatusCoupleJob();
                        DataService.init().getUserInfo(new DataService.onUserData() {
                          @Override public void onUserDataResult(UserDataResponse response) {
                            SettingsRepository.addUserInfo(realm, response.getUserInfo());

                            setResult(RESULT_OK);
                            finish();
                          }

                          @Override public void onUserDataError() {

                          }
                        }, PrefRepository.getToken(context));
                      }
                    }

                    @Override public void onGetPulseError(String error) {
                      checkCoupleStatus();
                    }
                  }, PrefRepository.getToken(BaseActivity.this));
                }
              }
            });
        jobScheduler.addJob(job);
      }
    });
  }

  public void removeCheckStatusCoupleJob() {

    SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
    if (!jobScheduler.contains(JobMessageService.JOB_PULSE_ACTIVE_ID)) {
      //Toast.makeText(this, "No job exists with JobID: " + JobMessageService.JOB_MESSAGE_ACTIVE_ID, Toast.LENGTH_SHORT).show();
      return;
    }

    if (jobScheduler.removeJob(JobMessageService.JOB_PULSE_ACTIVE_ID)) {
      //Toast.makeText(this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
    }
  }

  private void chooseUserTheme() {

    switch (PrefRepository.getTheme(this)) {
      case Themes.THEME_DEFAULT:
        setTheme(R.style.Theme_Pairtodo_Default);
        break;
      case Themes.THEME_PARIS:
        setTheme(R.style.Theme_Pairtodo_Paris);
        break;
      case Themes.THEME_SEA:
        setTheme(R.style.Theme_Pairtodo_Sea);
        break;
      case Themes.THEME_CAT:
        setTheme(R.style.Theme_Pairtodo_Cat);
        break;
      case Themes.THEME_ROAD:
        setTheme(R.style.Theme_Pairtodo_Road);
        break;
      case Themes.THEME_WITCH:
        setTheme(R.style.Theme_Pairtodo_Witch);
        break;
      case Themes.THEME_CUTE_PURPLE:
        setTheme(R.style.Theme_Pairtodo_CutePurple);
        break;
      case Themes.THEME_PURPLE_HAZE:
        setTheme(R.style.Theme_Pairtodo_PurpleHaze);
        break;
      case Themes.THEME_SUMMER:
        setTheme(R.style.Theme_Pairtodo_Summer);
        break;
      case Themes.THEME_BEAUTY:
        setTheme(R.style.Theme_Pairtodo_Beauty);
        break;
      case "":
        setTheme(R.style.Theme_Pairtodo_Default);
        break;
      default:
        setTheme(R.style.Theme_Pairtodo_Default);
    }
  }

  public void clearApplicationData() {

    SharedPreferences settings = getSharedPreferences("settings", 0);
    settings.edit().clear().commit();
    SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(this);
    defaultPref.edit().clear().commit();
    File cacheDirectory = getCacheDir();
    File applicationDirectory = new File(cacheDirectory.getParent());
    if (applicationDirectory.exists()) {

      String[] fileNames = applicationDirectory.list();

      for (String fileName : fileNames) {

        if (!fileName.equals("lib")) {

          deleteFile(new File(applicationDirectory, fileName));
        }
      }
    }
    eraseRealm();
  }

  public void eraseRealm() {
    realm.beginTransaction();
    realm.deleteAll();
    realm.commitTransaction();
  }
}
