package com.pairtodopremium.data;

import com.pairtodopremium.data.response.signup.BasicResponse;
import com.pairtodopremium.network.api.ApiManager;
import com.pairtodopremium.network.api.SignConfig;
import com.pairtodopremium.utils.TimeUtils;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Data service for http requests
 */

public class SignInApiService {

  private static SignInApiService signInApiService;

  public static SignInApiService init() {
    if (signInApiService == null) {
      signInApiService = new SignInApiService();
    }
    return signInApiService;
  }

  public void signUpUser(final onSignUpResult listener, String name, String email,
      String password) {
    ArrayList<String> args = new ArrayList<>();
    args.add(email);
    args.add(name);
    args.add(SignConfig.generateSHA1(password));
    long timeStamp = TimeUtils.getTimeStamp();
    args.add(String.valueOf(timeStamp));

    ApiManager.getApiService()
        .signUpUser(SignConfig.APP_ID, SignConfig.generateSig(args), SignConfig.APP_LANG,
            SignConfig.OS, email, name, SignConfig.generateSHA1(password), timeStamp)
        .enqueue(new Callback<BasicResponse>() {
          @Override
          public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
            listener.onSignUpResult(response.body());
          }

          @Override public void onFailure(Call<BasicResponse> call, Throwable t) {
            listener.onSignUpError(t.getLocalizedMessage());
          }
        });
  }

  public void signInUser(final onSignUpResult listener, String email, String password) {
    ArrayList<String> args = new ArrayList<>();
    args.add(email);
    args.add(SignConfig.generateSHA1(password));
    long timeStamp = TimeUtils.getTimeStamp();
    args.add(String.valueOf(timeStamp));

    ApiManager.getApiService()
        .signInUser(SignConfig.APP_ID, SignConfig.generateSig(args), SignConfig.APP_LANG,
            SignConfig.OS, email, SignConfig.generateSHA1(password), timeStamp)
        .enqueue(new Callback<BasicResponse>() {
          @Override
          public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
            listener.onSignUpResult(response.body());
          }

          @Override public void onFailure(Call<BasicResponse> call, Throwable t) {

            listener.onSignUpError(t.getMessage());
            int a = 5;
          }
        });
  }

  public void signInSocial(final onSignUpResult listener, String socialName, String id,
      String photo, String name) {
    ArrayList<String> args = new ArrayList<>();
    args.add(socialName);
    args.add(SignConfig.generateSHA1(id));
    args.add(name);
    args.add(photo);
    long timeStamp = TimeUtils.getTimeStamp();
    args.add(String.valueOf(timeStamp));

    ApiManager.getApiService()
        .signInUserVk(SignConfig.APP_ID, SignConfig.generateSig(args), SignConfig.APP_LANG,
            SignConfig.OS, socialName, SignConfig.generateSHA1(id), name, photo, timeStamp)
        .enqueue(new Callback<BasicResponse>() {
          @Override
          public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
            listener.onSignUpResult(response.body());
          }

          @Override public void onFailure(Call<BasicResponse> call, Throwable t) {
            listener.onSignUpError(t.getMessage());
          }
        });
  }

  public interface onSignUpResult {
    public void onSignUpResult(BasicResponse response);

    public void onSignUpError(String message);
  }
}
