package com.pairtodobeta.network.api.socialApi;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pairtodobeta.R;
import com.pairtodobeta.data.DataService;
import com.pairtodobeta.data.SignInApiService;
import com.pairtodobeta.data.response.signup.BasicResponse;
import com.pairtodobeta.data.response.signup.Result;
import com.pairtodobeta.data.response.userData.UserDataResponse;
import com.pairtodobeta.data.settings.SettingsRepository;
import com.pairtodobeta.router.ActivityRouter;
import com.pairtodobeta.db.PrefRepository;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;

import static com.pairtodobeta.ui.BaseActivity.realm;

public class VkApiManager {

    private static VkApiManager vkApiManager;

    public static VkApiManager init() {
        if (vkApiManager == null) {
            vkApiManager = new VkApiManager();
        }
        return vkApiManager;
    }

    public static void getUserInfo(final Activity activity){
        final int[] userId = {1};
        if(VKAccessToken.currentToken()!=null) {
            String userToken = VKAccessToken.currentToken().accessToken;
            VKRequest vkRequest = new VKRequest("users.get", VKParameters.from(VKApiConst.ACCESS_TOKEN,
                    userToken, VKApiConst.FIELDS, "first_name,last_name,photo_200,domain"));
            vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    super.attemptFailed(request, attemptNumber, totalAttempts);
                }

                @Override
                public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                    super.onProgress(progressType, bytesLoaded, bytesTotal);
                }

                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonObject = (JSONArray) response.json.get("response");
                        String s = jsonObject.getJSONObject(0).get("id").toString();
                        String firstName = jsonObject.getJSONObject(0).get("first_name").toString();
                        String lastName = jsonObject.getJSONObject(0).get("last_name").toString();
                        String domain = jsonObject.getJSONObject(0).get("domain").toString();
                        String avatarImage = jsonObject.getJSONObject(0).get("photo_200").toString();

                        userId[0] = gson.fromJson(s, new TypeToken<Integer>() {
                        }.getType());

                        String userName = firstName+" "+lastName;

                        SignInApiService.init().signInSocial(new SignInApiService.onSignUpResult() {
                            @Override
                            public void onSignUpResult(BasicResponse response) {
                                if (response.getResult() != null) {
                                    Result result = (Result)response.getResult();
                                    SettingsRepository.addUserSettings(realm,
                                            Integer.parseInt(result.getUserId()), result.getToken());
                                    PrefRepository.setToken(activity, result.getToken());
                                    PrefRepository.setUserId(activity, response.getResult().getUserId());
                                    PrefRepository.setIsAuthorized(activity);
                                    DataService.init().getUserInfo(new DataService.onUserData() {
                                        @Override
                                        public void onUserDataResult(UserDataResponse response) {
                                            SettingsRepository.addUserInfo(realm, response.getUserInfo());
                                            DataService.init().getUserInfo(new DataService.onUserData() {
                                                @Override
                                                public void onUserDataResult(UserDataResponse response) {
                                                    if (response.getUserInfo() != null) {
                                                        SettingsRepository.addUserInfo(realm, response.getUserInfo());
                                                        activity.finish();
                                                        ActivityRouter.startMainActivity(activity);
                                                    }
                                                }
                                                @Override
                                                public void onUserDataError() {

                                                }}, PrefRepository.getToken(activity));
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
                        }, "VK", String.valueOf(userId[0]), avatarImage , userName);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    String errorr = error.toString();
                    int a = 5;
                }
            });
        }
    }

}
