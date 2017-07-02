package com.pairtodopremium.ui.main.tasks;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.pairtodopremium.R;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
  private Context mContext;

  public TabPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    this.mContext = context;
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return MyTasksFragment.newInstance();

      case 1:
        return CoupleTasksFragment.newInstance();
    }

    return MyTasksFragment.newInstance();
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return mContext.getString(R.string.my_tasks);
      case 1:
        return mContext.getString(R.string.couple_tasks);
    }
    return "Dummy Tab " + (++position);
  }
}