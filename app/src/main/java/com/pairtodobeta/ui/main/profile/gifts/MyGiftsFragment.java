package com.pairtodobeta.ui.main.profile.gifts;

import android.view.View;

import com.pairtodobeta.data.DataService;
import com.pairtodobeta.data.response.gifts.Gift;
import com.pairtodobeta.data.settings.SettingsRepository;
import com.pairtodobeta.ui.BaseActivity;
import com.pairtodobeta.db.PrefRepository;

import java.util.List;

public class MyGiftsFragment extends BaseGiftsFragment {
    public static MyGiftsFragment newInstance() {
        return new MyGiftsFragment();
    }

    @Override
    protected void getGifts() {
        mGiftsSwipeContainer.setRefreshing(true);
        DataService.init().getGifts(new DataService.onGetGifts() {
            @Override
            public void onGetGiftsResult(List<Gift> gifts) {
                if (gifts.size() > 0) {
                    for (int i = 0; i < gifts.size(); i++) {
                        if (!gifts.get(i).fromId().equals(SettingsRepository.getUserInfo
                                (BaseActivity.realm).getPairId()))
                            gifts.remove(i);
                    }

                    GiftsAdapter adapter = new GiftsAdapter(getActivity(), gifts);
                    mGiftList.setAdapter(adapter);

                    mGiftsSwipeContainer.setRefreshing(false);
                } else {
                    noGiftsView.setVisibility(View.VISIBLE);
                    mGiftsSwipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onGetGiftsError() {

            }
        }, PrefRepository.getToken(getActivity()));
    }
}
