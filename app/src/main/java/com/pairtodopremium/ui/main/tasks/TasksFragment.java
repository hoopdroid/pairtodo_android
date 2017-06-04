package com.pairtodopremium.ui.main.tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pairtodopremium.R;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.main.BaseFragment;
import com.pairtodopremium.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TasksFragment extends BaseFragment {
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.detail_tabs)
    TabLayout mTabLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    TabPagerAdapter tabPagerAdapter;
    FragmentActivity mContext;

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_tasks, container, false);
        ButterKnife.bind(this, convertView);
        mContext = (FragmentActivity) getActivity();
        initTabs();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityRouter.startAddTaskActivity(getActivity(), Constants.CHANGE_TASK);
            }
        });
        return convertView;
    }

    private void initTabs() {
        tabPagerAdapter = new TabPagerAdapter(mContext, mContext.getSupportFragmentManager()
        );
        mViewPager.setAdapter(tabPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
