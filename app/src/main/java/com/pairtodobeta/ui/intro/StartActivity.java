package com.pairtodobeta.ui.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pairtodobeta.R;
import com.pairtodobeta.router.ActivityRouter;
import com.vk.sdk.util.VKUtil;


public class StartActivity extends AppCompatActivity {
    
    Button btnStart;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        btnStart = (Button) findViewById(R.id.btn_login);
        
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityRouter.startSignUpActivity(StartActivity.this);
                finish();
            }
        });

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        int a = 5;

    }
}
