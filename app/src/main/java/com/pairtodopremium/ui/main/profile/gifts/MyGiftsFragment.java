package com.pairtodopremium.ui.main.profile.gifts;

import android.view.View;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.gifts.Gift;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.ui.BaseActivity;
import java.util.List;

public class MyGiftsFragment extends BaseGiftsFragment {
  public static MyGiftsFragment newInstance() {
    return new MyGiftsFragment();
  }

  @Override protected void getGifts() {
    mGiftsSwipeContainer.setRefreshing(true);
    DataService.init().getGifts(new DataService.onGetGifts() {
      @Override public void onGetGiftsResult(List<Gift> gifts) {
        if (gifts.size() > 0) {
          for (int i = 0; i < gifts.size(); i++) {
            if (!gifts.get(i)
                .fromId()
                .equals(SettingsRepository.getUserInfo(BaseActivity.realm).getPairId())) {
              gifts.remove(i);
            }
          }

          GiftsAdapter adapter = new GiftsAdapter(getActivity(), gifts);
          mGiftList.setAdapter(adapter);

          mGiftsSwipeContainer.setRefreshing(false);
        } else {
          noGiftsView.setVisibility(View.VISIBLE);
          mGiftsSwipeContainer.setRefreshing(false);
        }
      }

      @Override public void onGetGiftsError() {

      }
    }, PrefRepository.getToken(getActivity()));
  }
}
