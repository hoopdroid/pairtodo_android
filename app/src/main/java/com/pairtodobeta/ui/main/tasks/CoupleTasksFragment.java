package com.pairtodobeta.ui.main.tasks;

import android.view.View;

import com.pairtodobeta.data.DataService;
import com.pairtodobeta.data.response.tasks.GetTasksResponse;
import com.pairtodobeta.data.response.userData.UserInfo;
import com.pairtodobeta.data.settings.SettingsRepository;
import com.pairtodobeta.ui.BaseActivity;
import com.pairtodobeta.utils.AnimationBuilderHelper;
import com.pairtodobeta.db.PrefRepository;

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
