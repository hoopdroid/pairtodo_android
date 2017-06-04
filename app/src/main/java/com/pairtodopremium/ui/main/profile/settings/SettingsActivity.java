package com.pairtodopremium.ui.main.profile.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.SettingsApiService;
import com.pairtodopremium.data.response.EmptyResultResponse;
import com.pairtodopremium.data.response.userData.UserInfo;
import com.pairtodopremium.data.response.userPhoto.UserImage;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.pairtodopremium.utils.FileUtil.reduceFile;

public class SettingsActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.userNameEditBtn)
    ImageView userNameEdit;
    @Bind(R.id.userNickname)
    TextView userNickName;
    @Bind(R.id.userNicknameEditBtn)
    ImageView userNickNameEdit;
    @Bind(R.id.userEmail)
    TextView userEmail;
    @Bind(R.id.userEmailEditBtn)
    ImageView userEmailEdit;
    @Bind(R.id.userPhotoEdit)
    ViewGroup userPhotoEdit;
    @Bind(R.id.removeCoupleBtn)
    ViewGroup removeCoupleBtn;
    @Bind(R.id.changeThemeBtn)
    ViewGroup changeThemeBtn;
    @Bind(R.id.logoutBtn)
    ViewGroup logoutBtn;
    @Bind(R.id.aboutAppBtn)
    ViewGroup aboutAppbtn;
    @Bind(R.id.photoUserMini)
    ImageView userPhotoMini;

    UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mToolbar.setTitle(R.string.settings);

        userInfo = SettingsRepository.getUserInfo(realm);

        if (userInfo !=null ) {
            userName.setText(userInfo.getName());
            userNickName.setText(userInfo.getNicName());
            userEmail.setText(userInfo.getEmail());
            Picasso.with(this).load(userInfo.getPhoto()).into(userPhotoMini);
        }

        userNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(getString(R.string.change_name), getString(R.string.please_enter_new_name), "Name");
            }
        });

        userNickNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getString(R.string.change_nick), getString(R.string.please_enter_new_nick), "Nickname");

            }
        });

        userEmailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getString(R.string.change_email), getString(R.string.please_enter_new_email), "Email");
            }
        });

        changeThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityRouter.startCategoryShopActivity(SettingsActivity.this, "themes");
            }
        });

        removeCoupleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsApiService.init().removeCouple(new SettingsApiService.onResult() {
                    @Override
                    public void onSignUpResult(EmptyResultResponse response) {
                        //TODO Erase all and open first screen
                        clearApplicationData();
                        ActivityRouter.startSignUpActivity(SettingsActivity.this);
                        finish();
                    }

                    @Override
                    public void onSignUpError(String message) {

                    }
                }, PrefRepository.getToken(SettingsActivity.this));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsApiService.init().logout(new SettingsApiService.onResult() {
                    @Override
                    public void onSignUpResult(EmptyResultResponse response) {
                        //TODO Erase all and open first screen
                        clearApplicationData();
                        ActivityRouter.startSignUpActivity(SettingsActivity.this);
                        finish();
                    }

                    @Override
                    public void onSignUpError(String message) {
                    }
                }, PrefRepository.getToken(SettingsActivity.this));
            }
        });

        userPhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openGallery(SettingsActivity.this, 0);
            }
        });
    }

    private void openDialog(String title, String message, final String parameter) {
        LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
        View subView = inflater.inflate(R.layout.dialog_layout, null);
        final EditText subEditText = (EditText) subView.findViewById(R.id.dialogEditText);
        final String token = PrefRepository.getToken(SettingsActivity.this);
        subEditText.setHint(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (parameter) {
                    case "Name":
                        SettingsApiService.init().changeValue(new SettingsApiService.onResult() {
                            @Override
                            public void onSignUpResult(EmptyResultResponse response) {
                                userName.setText(subEditText.getText().toString());
                            }

                            @Override
                            public void onSignUpError(String message) {

                            }
                        }, "name",subEditText.getText().toString().trim(), token);
                        break;
                    case "Nickname":
                        SettingsApiService.init().changeValue(new SettingsApiService.onResult() {
                            @Override
                            public void onSignUpResult(EmptyResultResponse response) {
                                userNickName.setText(subEditText.getText().toString());
                            }

                            @Override
                            public void onSignUpError(String message) {

                            }
                        }, "nic_name",subEditText.getText().toString().trim(), token);
                        break;
                    case "Email":
                        SettingsApiService.init().changeValue(new SettingsApiService.onResult() {
                            @Override
                            public void onSignUpResult(EmptyResultResponse response) {
                                userEmail.setText(subEditText.getText().toString());
                            }

                            @Override
                            public void onSignUpError(String message) {

                            }
                        }, "email",subEditText.getText().toString().trim(), token);
                        break;
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.PHOTO_FROM_SETTINGS:
                    break;
            }
            initGalleryCallback(requestCode, resultCode, data);
        } else {}
    }

    public void initGalleryCallback(int requestCode, int resultCode, Intent data){
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(SettingsActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                try {
                    DataService.init().uploadUserImage(reduceFile(imageFile), new DataService.onUploadUserPhoto() {
                        @Override
                        public void onUploadResult(UserImage image) {
                            Picasso.with(SettingsActivity.this).load(image.photo()).into(userPhotoMini);
                        }

                        @Override
                        public void onUploadError(String error) {
                        }
                    }, PrefRepository.getToken(SettingsActivity.this));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(SettingsActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }



}
