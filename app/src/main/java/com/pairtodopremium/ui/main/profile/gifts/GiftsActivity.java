package com.pairtodopremium.ui.main.profile.gifts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Bind;
import com.pairtodopremium.R;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.BaseActivity;

public class GiftsActivity extends BaseActivity {
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.viewpager) ViewPager mViewPager;
  @Bind(R.id.detail_tabs) TabLayout mTabLayout;
  @Bind(R.id.fab) FloatingActionButton sendNewGiftBtn;

  GiftPagerAdapter tabPagerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gifts);

    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });

    mToolbar.setTitle(R.string.gifts_name);

    initTabs();

    sendNewGiftBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ActivityRouter.startCategoryShopActivity(GiftsActivity.this, "gifts");
      }
    });
  }

  private void initTabs() {
    tabPagerAdapter = new GiftPagerAdapter(this, getSupportFragmentManager());
    mViewPager.setAdapter(tabPagerAdapter);
    mTabLayout.setupWithViewPager(mViewPager);
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        if (position == 0) {

        } else {

        }
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }
}
