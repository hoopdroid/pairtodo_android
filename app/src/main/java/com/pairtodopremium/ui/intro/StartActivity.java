package com.pairtodopremium.ui.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import com.pairtodopremium.R;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.BaseActivity;

public class StartActivity extends BaseActivity {

  Button btnStart;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_start);

    btnStart = (Button) findViewById(R.id.btn_login);

    btnStart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ActivityRouter.startSignUpActivity(StartActivity.this);
        finish();
      }
    });
  }
}
