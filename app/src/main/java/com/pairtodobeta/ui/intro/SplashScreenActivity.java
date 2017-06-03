package com.pairtodobeta.ui.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

import com.pairtodobeta.R;
import com.pairtodobeta.router.ActivityRouter;
import com.pairtodobeta.ui.BaseActivity;


public class SplashScreenActivity extends BaseActivity {

    public static final int LOADING_TIME = 1000;
    public static final int LOADING_INTERVAL = 200;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (settings.getInt("isAuthorized", 0) == 0) {
            Intent i = new Intent(this, StartActivity.class);
            startActivity(i);

        } else {
            setContentView(R.layout.activity_splash_screen);
            initViewElements();
            setLoading();
        }
    }

    private void setLoading() {

        countDownTimer = new CountDownTimer(LOADING_TIME, LOADING_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                SplashScreenActivity.this.finish();
                ActivityRouter.startMainActivity(getApplicationContext());
            }

        }.start();
    }

    void initViewElements() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (countDownTimer != null)
            countDownTimer.cancel();

    }
}
