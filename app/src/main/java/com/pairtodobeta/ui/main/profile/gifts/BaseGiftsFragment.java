package com.pairtodobeta.ui.main.profile.gifts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pairtodobeta.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseGiftsFragment extends
        Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv)
    RecyclerView mGiftList;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mGiftsSwipeContainer;
    @Bind(R.id.no_gifts_view)
    ViewGroup noGiftsView;

    public BaseGiftsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_gifts, container, false);

        initViewElements(convertView);

        if (isAdded())
            getGifts();

        return convertView;
    }

    private void initViewElements(View convertView) {
        ButterKnife.bind(this, convertView);
        mGiftList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mGiftList.setLayoutManager(layoutManager);
        mGiftsSwipeContainer.setOnRefreshListener(this);


    }

    @Override
    public void onRefresh() {
        getGifts();
    }

    protected abstract void getGifts();


}
