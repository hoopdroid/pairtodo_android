package com.pairtodopremium.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.github.chrisbanes.photoview.PhotoView;
import com.pairtodopremium.R;
import com.pairtodopremium.utils.Constants;
import com.squareup.picasso.Picasso;

public class PhotoViewActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_view);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
    String photo = getIntent().getStringExtra("Photo");
    int comment = getIntent().getIntExtra("Comment", 0);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (comment == 0) photoView.setTransitionName(Constants.IMAGE_TRANSITION);
    }
    Picasso.with(this).load(photo).into(photoView);
  }
}
