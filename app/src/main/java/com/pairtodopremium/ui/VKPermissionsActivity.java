package com.pairtodopremium.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.pairtodopremium.R;
import com.pairtodopremium.network.api.socialApi.VkApiManager;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class VKPermissionsActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vkpermissions);
    VKSdk.login(this, VKScope.EMAIL);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
      @Override public void onResult(VKAccessToken res) {
        VkApiManager.getUserInfo(VKPermissionsActivity.this);
      }

      @Override public void onError(VKError error) {
      }
    })) {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
