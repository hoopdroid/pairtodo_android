package com.pairtodopremium.ui.intro.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import butterknife.Bind;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pairtodopremium.R;
import com.pairtodopremium.network.api.socialApi.FacebookApiManager;
import com.pairtodopremium.ui.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookActivity extends BaseActivity {

  @Bind(R.id.login_button) LoginButton loginButton;
  private CallbackManager callbackManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_facebook);

    loginButton.setReadPermissions("email", "public_profile");
    callbackManager = CallbackManager.Factory.create();
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override public void onSuccess(LoginResult loginResult) {
        // App code
        setFacebookData(loginResult);
      }

      @Override public void onCancel() {
        // App code
        int a = 5;
      }

      @Override public void onError(FacebookException exception) {
        // App code
        int a = 5;
      }
    });
  }

  private void setFacebookData(final LoginResult loginResult) {
    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
        new GraphRequest.GraphJSONObjectCallback() {
          @Override public void onCompleted(JSONObject object, GraphResponse response) {
            // Application code
            try {
              Log.i("Response", response.toString());

              String email = response.getJSONObject().getString("email");
              String lastName = response.getJSONObject().getString("last_name");
              String firstName = response.getJSONObject().getString("first_name") + " " + lastName;
              String photo = "https://graph.facebook.com/"
                  + loginResult.getAccessToken().getUserId()
                  + "/picture?type=large";

              FacebookApiManager.signInFacebook(FacebookActivity.this, firstName, email, photo);

              Log.i("Login" + "Email", email);
              Log.i("Login" + "FirstName", firstName);
              Log.i("Login" + "LastName", lastName);
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        });

    Bundle parameters = new Bundle();
    parameters.putString("fields", "email, last_name, first_name");
    request.setParameters(parameters);
    request.executeAsync();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
