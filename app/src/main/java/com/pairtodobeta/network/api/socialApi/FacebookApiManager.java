package com.pairtodobeta.network.api.socialApi;

import android.app.Activity;
import android.widget.Toast;

import com.pairtodobeta.R;
import com.pairtodobeta.data.DataService;
import com.pairtodobeta.data.SignInApiService;
import com.pairtodobeta.data.response.signup.BasicResponse;
import com.pairtodobeta.data.response.signup.Result;
import com.pairtodobeta.data.response.userData.UserDataResponse;
import com.pairtodobeta.data.settings.SettingsRepository;
import com.pairtodobeta.router.ActivityRouter;
import com.pairtodobeta.db.PrefRepository;

import static com.pairtodobeta.ui.BaseActivity.realm;

public class FacebookApiManager {

    private static FacebookApiManager vkApiManager;

    public static FacebookApiManager init() {
        if (vkApiManager == null) {
            vkApiManager = new FacebookApiManager();
        }
        return vkApiManager;
    }

    public static void signInFacebook(final Activity activity, String name, String email, String photo) {

        SignInApiService.init().signInSocial(new SignInApiService.onSignUpResult() {
            @Override
            public void onSignUpResult(BasicResponse response) {
                if (response.getResult() != null) {
                    Result result = (Result) response.getResult();
                    SettingsRepository.addUserSettings(realm,
                            Integer.parseInt(result.getUserId()), result.getToken());
                    PrefRepository.setToken(activity, result.getToken());
                    PrefRepository.setUserId(activity, response.getResult().getUserId());
                    DataService.init().getUserInfo(new DataService.onUserData() {
                        @Override
                        public void onUserDataResult(UserDataResponse response) {
                            if (response.getUserInfo() != null) {
                                PrefRepository.setIsAuthorized(activity);
                                SettingsRepository.addUserInfo(realm, response.getUserInfo());
                                activity.finish();
                                ActivityRouter.startMainActivity(activity);
                            }
                        }

                        @Override
                        public void onUserDataError() {

                        }
                    }, PrefRepository.getToken(activity));
                }
            }

            @Override
            public void onSignUpError(String message) {
                Toast.makeText(activity, R.string.correct_login_password, Toast.LENGTH_LONG).show();
            }
        }, "FB", email, photo, name);

    }
}

