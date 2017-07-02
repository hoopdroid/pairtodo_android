package com.pairtodopremium.ui.main.profile.noCouple;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.invite.InviteResponse;
import com.pairtodopremium.data.response.userData.UserDataResponse;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.network.api.SignConfig;
import com.pairtodopremium.ui.BaseActivity;

public class SearchCoupleActivity extends BaseActivity {
  public static final int PICK_MY_CONTACT = 001;
  public static final int PICK_COUPLE_CONTACT = 004;
  public static final int REQUEST_READ_CONTACTS = 002;

  @Bind(R.id.toolbar) Toolbar mToolbar;

  @Bind(R.id.input_phone) EditText inputCouplePhone;
  @Bind(R.id.input_my_phone) EditText inputMyPhone;
  @Bind(R.id.btn_confirm_send_couple) Button btnConfirmSendCouple;
  @Bind(R.id.searchMyNumber) View searchMyNumber;
  @Bind(R.id.searchCoupleNumber) View searchCoupleNumber;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_couple);

    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });

    if (!PrefRepository.getCouplePhone(this).isEmpty() && !PrefRepository.getMyPhone(this)
        .isEmpty()) {
      mToolbar.setTitle("Ожидание партнера");
      inputMyPhone.setText(PrefRepository.getMyPhone(this));
      inputCouplePhone.setText(PrefRepository.getCouplePhone(this));
      btnConfirmSendCouple.setText("Отправить еще раз");
    } else {
      mToolbar.setTitle(R.string.search_couple);
      btnConfirmSendCouple.setText("Отправить приглашение");
    }

    btnConfirmSendCouple.setVisibility(View.VISIBLE);
    btnConfirmSendCouple.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        PrefRepository.setCouplePhone(SearchCoupleActivity.this,
            inputCouplePhone.getText().toString().trim());
        PrefRepository.setMyPhone(SearchCoupleActivity.this,
            inputMyPhone.getText().toString().trim());

        DataService.init().sendInviteCode(new DataService.onInviteCouple() {
                                            @Override public void onInviteResult(InviteResponse inviteResponse) {
                                              if (inviteResponse.getResult().toString().isEmpty()) {
                                                checkCoupleStatus();
                                                mToolbar.setTitle("Ожидание партнера");
                                                btnConfirmSendCouple.setText("Отправить еще раз");
                                                Toast.makeText(SearchCoupleActivity.this, R.string.send_invitation,
                                                    Toast.LENGTH_SHORT).show();
                                              } else {
                                                DataService.init().getUserInfo(new DataService.onUserData() {
                                                  @Override public void onUserDataResult(UserDataResponse response) {
                                                    SettingsRepository.addUserInfo(realm, response.getUserInfo());

                                                    setResult(RESULT_OK);
                                                    finish();
                                                  }

                                                  @Override public void onUserDataError() {

                                                  }
                                                }, PrefRepository.getToken(SearchCoupleActivity.this));
                                              }
                                            }

                                            @Override public void onInviteError() {
                                              Toast.makeText(SearchCoupleActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT)
                                                  .show();
                                            }
                                          }, PrefRepository.getToken(SearchCoupleActivity.this),
            SignConfig.generateSHA1(inputMyPhone.getText().toString().trim()),
            SignConfig.generateSHA1(inputCouplePhone.getText().toString().trim()));
      }
    });

    searchMyNumber.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContact(PICK_MY_CONTACT);
      }
    });

    searchCoupleNumber.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContact(PICK_COUPLE_CONTACT);
      }
    });
  }

  private void getContact(int request) {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    /*If Android M*/
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
          != PackageManager.PERMISSION_GRANTED) {
        /*If Android M and Not permission granted */
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_CONTACTS },
            REQUEST_READ_CONTACTS);
      } else {
        /*If Android M and permission granted */
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, request);
      }
    } else {
    /*IF not Android M*/
      Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
      startActivityForResult(intent, request);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            String phoneNumber =
                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (Integer.parseInt(phoneNumber) > 0) {
              Cursor phones =
                  getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                      null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                      null, null);
              while (phones.moveToNext()) {
                number = phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                break;
              }
              phones.close();
            }
          }

          inputMyPhone.setText(formatNumber(number));
        }
        break;

      case (PICK_COUPLE_CONTACT):
        if (resultCode == Activity.RESULT_OK) {
          Uri contactData = data.getData();
          Cursor c = this.getContentResolver().query(contactData, null, null, null, null);
          if (c.moveToFirst()) {
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber =
                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (Integer.parseInt(phoneNumber) > 0) {
              Cursor phones =
                  getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                      null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                      null, null);
              while (phones.moveToNext()) {
                number = phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                break;
              }
              phones.close();
            }
          }

          inputCouplePhone.setText(formatNumber(number));
        }
        break;
    }
  }

  private String formatNumber(String number) {
    if (number.startsWith("+")) number = number.substring(1, number.length() - 1);

    return number;
  }

  @Override public void onBackPressed() {
    setResult(RESULT_OK);
    super.onBackPressed();
  }
}
