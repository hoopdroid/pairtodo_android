package com.pairtodopremium.ui.main.profile.noCouple;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pairtodopremium.R;


public class SearchCouplePagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public SearchCouplePagerAdapter(Context context, FragmentManager fm){
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SearchByPhoneFragment.newInstance();
//            case 1:
//                return SearchByNameFragment.newInstance();
        }

        return SearchByNameFragment.newInstance();

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.by_phone);
//            case 1:
//                return "По имени или email";
        }
        return "Dummy Tab "+(++position) ;
    }
}