package com.pairtodopremium.ui.main.tasks;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pairtodopremium.R;
import com.pairtodopremium.db.PairToDoCacheDao;
import com.pairtodopremium.network.api.ApiManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseTasksFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.no_internet_view)
    ViewGroup noInternetView;
    @Bind(R.id.no_tasks_view)
    ViewGroup noTasksView;


    protected PairToDoCacheDao cacheManager = new PairToDoCacheDao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_my_tasks, container, false);
        initViewElements(convertView);

        if (isAdded())
            getTasks();

        return convertView;
    }

    private void initViewElements(View convertView) {
        ButterKnife.bind(this, convertView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDetach() {
        ApiManager.cancelAllRequests();
        super.onDetach();
    }

    protected abstract void getTasks();

    @Override
    public void onRefresh() {
        getTasks();
    }

    protected void setNoInternetView(){
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        noTasksView.setVisibility(View.GONE);
        noInternetView.setVisibility(View.VISIBLE);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
