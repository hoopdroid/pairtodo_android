package com.pairtodopremium.ui.main.shop;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.entities.shop.StorageGift;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.db.PrefRepository;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

public class SendGiftActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.giftName)
    EditText giftNameEdit;
    @Bind(R.id.giftImage)
    ImageView giftImage;
    @Bind(R.id.sendGiftBtn)
    Button sendGiftBtn;

    private StorageGift sendedStorageGift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendgift);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendedStorageGift = getIntent().getParcelableExtra("StorageGift");

        if (sendedStorageGift != null) {
            Picasso.with(this).load(sendedStorageGift.getFull()).into(giftImage);
            sendGiftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataService.init().sendGift(new DataService.onMessageTask() {
                        @Override
                        public void onMessageResult() {
                            //Toast.makeText(SendGiftActivity.this, R.string.gift_sent, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onMessageError() {
                            //Toast.makeText(SendGiftActivity.this, R.string.gift_not_sent_try_again, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                }, PrefRepository.getToken(SendGiftActivity.this), giftNameEdit.getText().toString().trim(), sendedStorageGift.getTovarName());
                }
            });

        }


    }

}
