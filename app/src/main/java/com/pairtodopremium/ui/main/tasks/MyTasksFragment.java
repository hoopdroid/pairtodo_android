package com.pairtodopremium.ui.main.tasks;

import android.view.View;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.tasks.GetTasksResponse;
import com.pairtodopremium.data.response.userData.UserInfo;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.utils.AnimationBuilderHelper;

public class MyTasksFragment extends BaseTasksFragment {
  public MyTasksFragment() {
  }

  public static MyTasksFragment newInstance() {
    return new MyTasksFragment();
  }

  @Override protected void getTasks() {
    mSwipeRefreshLayout.setRefreshing(true);
    if (isNetworkAvailable()) {
      DataService.init().getMyTasks(new DataService.onGetMyTasks() {
        @Override public void onGetMyTasksResult(GetTasksResponse response) {
          if (isAdded()) {
            UserInfo info = SettingsRepository.getUserInfo(BaseActivity.realm);
            if (info != null) {
              MyTasksAdapter adapter = new MyTasksAdapter(getActivity(), response.getResult(),
                  SettingsRepository.getUserInfo(BaseActivity.realm).getId());
              mRecyclerView.setAdapter(adapter);
              mSwipeRefreshLayout.setRefreshing(false);

              if (response.getResult().size() == 0) noTasksView.setVisibility(View.VISIBLE);

              AnimationBuilderHelper.startRVAnimation(getActivity(), mRecyclerView);
            }
          }
        }

        @Override public void onGetMyTasksError() {
          // TODO Show error view
        }
      }, PrefRepository.getToken(getActivity()));
    } else {
      setNoInternetView();
    }
  }
}
