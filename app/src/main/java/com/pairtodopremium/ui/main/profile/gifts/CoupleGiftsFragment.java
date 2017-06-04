package com.pairtodopremium.ui.main.profile.gifts;

import android.view.View;

import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.gifts.Gift;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.db.PrefRepository;

import java.util.List;

public class CoupleGiftsFragment extends BaseGiftsFragment {
    public static CoupleGiftsFragment newInstance() {
        return new CoupleGiftsFragment();
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
                                (BaseActivity.realm).getId()))
                            gifts.remove(i);
                    }

                    GiftsAdapter adapter = new GiftsAdapter(getActivity(), gifts);
                    mGiftList.setAdapter(adapter);

                    mGiftsSwipeContainer.setRefreshing(false);
                }
                else {
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

