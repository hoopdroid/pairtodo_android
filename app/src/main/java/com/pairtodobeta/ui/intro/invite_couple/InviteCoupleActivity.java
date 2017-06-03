package com.pairtodobeta.ui.intro.invite_couple;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pairtodobeta.R;
import com.pairtodobeta.data.DataService;
import com.pairtodobeta.data.entities.SettingsObject;
import com.pairtodobeta.data.response.invite.InviteResponse;
import com.pairtodobeta.data.response.userData.UserDataResponse;
import com.pairtodobeta.data.settings.SettingsRepository;
import com.pairtodobeta.router.ActivityRouter;
import com.pairtodobeta.ui.BaseActivity;
import com.pairtodobeta.db.PrefRepository;

import butterknife.Bind;

/**
 * Created by ilyasavin on 2/4/17.
 */

public class InviteCoupleActivity extends BaseActivity {

    public static final int PICK_MY_CONTACT = 001;
    public static final int PICK_COUPLE_CONTACT = 004;
    public static final int REQUEST_READ_CONTACTS = 002;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.input_phone)
    EditText inputPhone;
    @Bind(R.id.btn_invite_couple)
    Button btnCoupleContact;
    @Bind(R.id.btn_confirm_send_couple)
    Button btnConfirmSendCouple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_couple);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnConfirmSendCouple.setVisibility(View.VISIBLE);

        mToolbar.setTitle(R.string.step_two_invite);

        btnCoupleContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCoupleFromContact();
            }
        });

        btnConfirmSendCouple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SettingsObject settingsObject = SettingsRepository.getUserSettings(realm);
                PrefRepository.setIsAuthorized(InviteCoupleActivity.this);

                DataService.init().sendInviteCode(new DataService.onInviteCouple() {
                    @Override
                    public void onInviteResult(InviteResponse inviteResponse) {
                        Toast.makeText(InviteCoupleActivity.this, inviteResponse.getResult().toString(), Toast.LENGTH_SHORT).show();
                        DataService.init().getUserInfo(new DataService.onUserData() {
                            @Override
                            public void onUserDataResult(UserDataResponse response) {
                                SettingsRepository.addUserInfo(realm, response.getUserInfo());
                                ActivityRouter.startMainActivity(InviteCoupleActivity.this);
                                finish();
                            }

                            @Override
                            public void onUserDataError() {

                            }
                        }, PrefRepository.getToken(InviteCoupleActivity.this));
                    }

                    @Override
                    public void onInviteError() {
                        // TODO Show error view
                    }
                }, settingsObject.getUserToken(), SettingsRepository.getUserSettings(realm).getUserPhone(), inputPhone.getText().toString());

            }
        });


        //inputPhone.addTextChangedListener(listener);
        //inputPhone.setOnFocusChangeListener(listener);

    }

    private void getCoupleFromContact() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    /*If Android M*/
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
        /*If Android M and Not permission granted */
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            } else {
        /*If Android M and permission granted */
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_MY_CONTACT);
            }
        } else {
    /*IF not Android M*/
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_MY_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String number = "";
        switch (requestCode) {
            case (PICK_MY_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = this.getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (Integer.parseInt(phoneNumber) > 0) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                btnConfirmSendCouple.setVisibility(View.VISIBLE);
                                break;
                            }
                            phones.close();
                        }
                    }
                }
                break;
        }

        inputPhone.setText(number);
    }
}
