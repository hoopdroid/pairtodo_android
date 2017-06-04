package com.pairtodopremium.network.api.socialApi;

import android.app.Activity;
import android.widget.Toast;

import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.SignInApiService;
import com.pairtodopremium.data.response.signup.BasicResponse;
import com.pairtodopremium.data.response.signup.Result;
import com.pairtodopremium.data.response.userData.UserDataResponse;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.db.PrefRepository;

import static com.pairtodopremium.ui.BaseActivity.realm;

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

