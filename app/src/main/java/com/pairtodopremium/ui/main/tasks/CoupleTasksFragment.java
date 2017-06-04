package com.pairtodopremium.ui.main.tasks;

import android.view.View;

import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.tasks.GetTasksResponse;
import com.pairtodopremium.data.response.userData.UserInfo;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.utils.AnimationBuilderHelper;
import com.pairtodopremium.db.PrefRepository;

public class CoupleTasksFragment extends BaseTasksFragment {
    public CoupleTasksFragment() {
    }

    public static CoupleTasksFragment newInstance() {
        return new CoupleTasksFragment();
    }

    @Override
    protected void getTasks() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (isNetworkAvailable()) {
            DataService.init().getMyTasks(new DataService.onGetMyTasks() {
                @Override
                public void onGetMyTasksResult(GetTasksResponse response) {
                    if (isAdded()) {
                        UserInfo info = SettingsRepository.getUserInfo(BaseActivity.realm);
                        if(info != null) {
                            CoupleTasksAdapter adapter = new CoupleTasksAdapter(getActivity(), response.getResult(),
                                    SettingsRepository.getUserInfo(BaseActivity.realm).getPairId());
                            mRecyclerView.setAdapter(adapter);
                            mSwipeRefreshLayout.setRefreshing(false);

                            if (response.getResult().size() == 0)
                                noTasksView.setVisibility(View.VISIBLE);

                            AnimationBuilderHelper.startRVAnimation(getActivity(), mRecyclerView);
                        }

                    }

                }

                @Override
                public void onGetMyTasksError() {

                }
            }, PrefRepository.getToken(getActivity()));
        }
        else {
            setNoInternetView();
        }
    }

}
