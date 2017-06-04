package com.pairtodopremium.ui.intro.sign_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;

import butterknife.Bind;

public class SignUpActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.input_name)
    EditText inputNameEdit;
    @Bind(R.id.input_email)
    EditText inputEmaiEdit;
    @Bind(R.id.input_password)
    EditText inputPasswordEdit;
    @Bind(R.id.registrationLayout)
    ViewGroup registrationLayout;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.btn_sign_in)
    Button signInBtn;
    @Bind(R.id.btn_sign_up)
    Button signUpBtn;
    @Bind(R.id.input_layout_password)
    TextInputLayout inputPasswordLayout;
    @Bind(R.id.input_layout_name)
    TextInputLayout inputNameLayout;
    @Bind(R.id.sign_content)
    ViewGroup signContent;

    public static boolean isdigit(EditText input) {

        String data = input.getText().toString().trim();
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isDigit(data.charAt(i)))
                return false;

        }
        return true;
    }

    public static boolean ischar(EditText input) {

        String data = input.getText().toString().trim();
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isDigit(data.charAt(i)))
                return true;

        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        inputNameEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        inputPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mToolbar.setTitle(R.string.sign_up_step_one_title);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        inputPasswordEdit.addTextChangedListener(new MyTextWatcher(inputPasswordEdit));
        inputNameEdit.addTextChangedListener(new MyTextWatcher(inputNameEdit));
        inputEmaiEdit.addTextChangedListener(new MyTextWatcher(inputEmaiEdit));
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityRouter.startSignInActivity(SignUpActivity.this);
                finish();
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    signUpRest();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void signUpRest() throws UnsupportedEncodingException {

        if (validateName() && validatePassword()) {

            mProgressBar.setVisibility(View.VISIBLE);
            signContent.setVisibility(View.INVISIBLE);

            SignInApiService.init().signUpUser(
                    new SignInApiService.onSignUpResult() {
                        @Override
                        public void onSignUpResult(BasicResponse response) {
                            if (response.getResult() != null) {
                                Result result = (Result) response.getResult();
                                SettingsRepository.addUserSettings(realm, Integer.parseInt(result.getUserId()),
                                        result.getToken());
                                PrefRepository.setToken(SignUpActivity.this, result.getToken());
                                PrefRepository.setIsAuthorized(SignUpActivity.this);
                                PrefRepository.setUserId(SignUpActivity.this, response.getResult().getUserId());
                                PairTodoApplication.checkPulseInBackground(getApplicationContext(), result.getToken());
                                DataService.init().getUserInfo(new DataService.onUserData() {
                                    @Override
                                    public void onUserDataResult(UserDataResponse response) {
                                        if (response.getUserInfo() != null) {
                                            signContent.setVisibility(View.VISIBLE);
                                            mProgressBar.setVisibility(View.GONE);
                                            SettingsRepository.addUserInfo(realm, response.getUserInfo());
                                            finish();
                                            ActivityRouter.startMainActivity(SignUpActivity.this);
                                        }
                                    }

                                    @Override
                                    public void onUserDataError() {
                                        signContent.setVisibility(View.VISIBLE);

                                    }
                                }, PrefRepository.getToken(SignUpActivity.this));
                            } else {
                                Toast.makeText(SignUpActivity.this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                                signContent.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onSignUpError(String message) {
                            mProgressBar.setVisibility(View.GONE);
                            signContent.setVisibility(View.VISIBLE);
                            Toast.makeText(SignUpActivity.this, R.string.correct_login_password, Toast.LENGTH_SHORT).show();
                        }
                    }, inputNameEdit.getText().toString().trim(), inputEmaiEdit.getText().toString().trim(),
                    inputPasswordEdit.getText().toString().trim());

        } else
            Toast.makeText(this, R.string.please_enter_correct_values, Toast.LENGTH_SHORT).show();

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

    private boolean validateName() {
        if (inputNameEdit.getText().toString().trim().isEmpty() || !ischar(inputNameEdit)) {
            inputNameLayout.setError(getString(R.string.name_in_latin));
            requestFocus(inputNameEdit);
            return false;
        } else {
            inputNameLayout.setErrorEnabled(false);
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
                case R.id.input_name:
                    validateName();
                    break;
            }
        }
    }

}
