package com.pairtodobeta.ui.main.tasks;

import android.view.View;

import com.pairtodobeta.data.DataService;
import com.pairtodobeta.data.response.tasks.GetTasksResponse;
import com.pairtodobeta.data.response.userData.UserInfo;
import com.pairtodobeta.data.settings.SettingsRepository;
import com.pairtodobeta.ui.BaseActivity;
import com.pairtodobeta.utils.AnimationBuilderHelper;
import com.pairtodobeta.db.PrefRepository;

public class MyTasksFragment extends BaseTasksFragment {
    public MyTasksFragment() {
    }

    public static MyTasksFragment newInstance() {
        return new MyTasksFragment();
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
                            MyTasksAdapter adapter = new MyTasksAdapter(getActivity(), response.getResult(),
                                    SettingsRepository.getUserInfo(BaseActivity.realm).getId());
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
                    // TODO Show error view
                }
            }, PrefRepository.getToken(getActivity()));
        }
        else {
            setNoInternetView();
        }
    }


}
