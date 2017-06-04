package com.pairtodopremium.ui.intro.sign_in;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pairtodopremium.PairTodoApplication;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.SignInApiService;
import com.pairtodopremium.data.response.signup.BasicResponse;
import com.pairtodopremium.data.response.signup.Result;
import com.pairtodopremium.data.response.userData.UserDataResponse;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.db.PrefRepository;

import butterknife.Bind;

public class SignInActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.btn_sign_in)
    Button btnSignIn;
    @Bind(R.id.input_email)
    EditText inputEmaiEdit;
    @Bind(R.id.input_password)
    EditText inputPasswordEdit;
    @Bind(R.id.input_layout_password)
    TextInputLayout inputPasswordLayout;
    @Bind(R.id.input_layout_email)
    TextInputLayout inputEmailLayout;
    @Bind(R.id.vk_auth)
    ImageView vkAuth;
    @Bind(R.id.fb_auth)
    ImageView fbAuth;
    @Bind(R.id.sign_content)
    ViewGroup signContent;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signin);

        mToolbar.setTitle(R.string.sign_in_title);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        vkAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityRouter.startVkPermissionsActivity(SignInActivity.this);
            }
        });

        fbAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityRouter.startFacebookActivity(SignInActivity.this);
            }
        });

    }


    private void signIn() {
        if (validateEmail() && validateEmail()) {
            signContent.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            SignInApiService.init().signInUser(new SignInApiService.onSignUpResult() {
                @Override
                public void onSignUpResult(BasicResponse response) {
                    if (response.getResult() != null) {
                        Result result = (Result)response.getResult();
                        SettingsRepository.addUserSettings(realm, Integer.parseInt(result.getUserId()), result.getToken());
                        PrefRepository.setIsAuthorized(SignInActivity.this);
                        PrefRepository.setUserId(SignInActivity.this, response.getResult().getUserId());
                        PairTodoApplication.checkPulseInBackground(getApplicationContext(), result.getToken());
                        PrefRepository.setToken(SignInActivity.this, result.getToken());
                        DataService.init().getUserInfo(new DataService.onUserData() {
                            @Override
                            public void onUserDataResult(UserDataResponse response) {
                                if (response.getUserInfo() != null) {
                                    mProgressBar.setVisibility(View.GONE);
                                    signContent.setVisibility(View.VISIBLE);
                                    SettingsRepository.addUserInfo(realm, response.getUserInfo());
                                    finish();
                                    ActivityRouter.startMainActivity(SignInActivity.this);
                            }
                        }
                            @Override
                            public void onUserDataError() {
                                mProgressBar.setVisibility(View.GONE);
                                signContent.setVisibility(View.VISIBLE);

                            }}, PrefRepository.getToken(SignInActivity.this));
                    }
                    else {
                        Toast.makeText(SignInActivity.this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        signContent.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onSignUpError(String message) {
                    mProgressBar.setVisibility(View.GONE);
                    signContent.setVisibility(View.VISIBLE);
                    Toast.makeText(SignInActivity.this, R.string.correct_login_password, Toast.LENGTH_SHORT).show();
                }
            }, inputEmaiEdit.getText().toString().trim(), inputPasswordEdit.getText().toString().trim());
        } else Toast.makeText(this, getString(R.string.please_enter_correct_values
        ), Toast.LENGTH_SHORT).show();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePassword() {
        if (inputPasswordEdit.getText().toString().trim().isEmpty()) {
            inputPasswordLayout.setError(getString(R.string.enter_correct_password));
            requestFocus(inputPasswordEdit);
            return false;
        } else {
            inputPasswordLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        if (inputEmaiEdit.getText().toString().trim().isEmpty()) {
            inputEmailLayout.setError(getString(R.string.please_enter_correct_values));
            requestFocus(inputEmailLayout);
            return false;
        } else {
            inputEmailLayout.setErrorEnabled(false);
        }

        return true;
    }

    public class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
            }
        }
    }
}
