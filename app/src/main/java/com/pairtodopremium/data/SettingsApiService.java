package com.pairtodopremium.data;

import com.pairtodopremium.data.response.EmptyResultResponse;
import com.pairtodopremium.network.api.ApiManager;
import com.pairtodopremium.network.api.SignConfig;
import com.pairtodopremium.utils.TimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ilyasavin on 2/4/17.
 */

public class SettingsApiService {

  private static SettingsApiService signInApiService;

  public static SettingsApiService init() {
    if (signInApiService == null) {
      signInApiService = new SettingsApiService();
    }
    return signInApiService;
  }

  public void changeValue(final onResult listener, String parameter, String value, String token) {
    ArrayList<String> args = new ArrayList<>();
    args.add(token);
    long timeStamp = TimeUtils.getTimeStamp();
    args.add(String.valueOf(timeStamp));

    HashMap<String, String> map = new HashMap<>();
    map.put(parameter, value);

    ApiManager.getApiService()
        .changeValues(SignConfig.APP_ID, SignConfig.generateSig(args), SignConfig.APP_LANG,
            SignConfig.OS, map, token, timeStamp)
        .enqueue(new Callback<EmptyResultResponse>() {
          @Override public void onResponse(Call<EmptyResultResponse> call,
              Response<EmptyResultResponse> response) {
            listener.onSignUpResult(response.body());
          }

          @Override public void onFailure(Call<EmptyResultResponse> call, Throwable t) {
            listener.onSignUpError(t.getMessage());
          }
        });
  }

  public void logout(final onResult listener, String token) {
    ArrayList<String> args = new ArrayList<>();
    long timeStamp = TimeUtils.getTimeStamp();
    args.add(token);
    args.add(String.valueOf(timeStamp));

    ApiManager.getApiService()
        .logout(SignConfig.APP_ID, SignConfig.generateSig(args), SignConfig.APP_LANG, SignConfig.OS,
            token, timeStamp)
        .enqueue(new Callback<EmptyResultResponse>() {
          @Override public void onResponse(Call<EmptyResultResponse> call,
              Response<EmptyResultResponse> response) {
            listener.onSignUpResult(response.body());
          }

          @Override public void onFailure(Call<EmptyResultResponse> call, Throwable t) {
            listener.onSignUpError(t.getMessage());
          }
        });
  }

  public void removeCouple(final onResult listener, String token) {
    ArrayList<String> args = new ArrayList<>();
    long timeStamp = TimeUtils.getTimeStamp();
    args.add(token);
    args.add(String.valueOf(timeStamp));

    ApiManager.getApiService()
        .removeCouple(SignConfig.APP_ID, SignConfig.generateSig(args), SignConfig.APP_LANG,
            SignConfig.OS, token, timeStamp)
        .enqueue(new Callback<EmptyResultResponse>() {
          @Override public void onResponse(Call<EmptyResultResponse> call,
              Response<EmptyResultResponse> response) {
            listener.onSignUpResult(response.body());
          }

          @Override public void onFailure(Call<EmptyResultResponse> call, Throwable t) {
            listener.onSignUpError(t.getMessage());
          }
        });
  }

  public interface onResult {
    public void onSignUpResult(EmptyResultResponse response);

    public void onSignUpError(String message);
  }
}
