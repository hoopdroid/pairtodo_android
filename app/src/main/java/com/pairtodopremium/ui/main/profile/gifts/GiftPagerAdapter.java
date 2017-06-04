package com.pairtodopremium.ui.main.profile.gifts;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pairtodopremium.R;

public class GiftPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public GiftPagerAdapter(Context context, FragmentManager fm){
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyGiftsFragment.newInstance();

            case 1:
                return CoupleGiftsFragment.newInstance();

        }

        return MyGiftsFragment.newInstance();

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.my_gifts_tab);
            case 1:
                return context.getString(R.string.couples_gifts);
        }
        return "Dummy Tab "+(++position) ;
    }
}