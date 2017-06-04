package com.pairtodopremium.ui.main.profile.stats;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.stats.StatsData;
import com.pairtodopremium.data.response.userData.UserInfo;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.db.PrefRepository;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.Bind;

public class StatsActivity extends BaseActivity {
    @Bind(R.id.userDoneTasks)
    TextView userDoneTasks;
    @Bind(R.id.userExpiredTasks)
    TextView userExpiredTasks;
    @Bind(R.id.userNotExecuted)
    TextView userNotExecutedTasks;

    @Bind(R.id.pairDoneTasks)
    TextView pairDoneTasks;
    @Bind(R.id.pairExpiredTasks)
    TextView pairExpiredTasks;
    @Bind(R.id.pairNotExecuted)
    TextView pairNotExecutedTasks;

    @Bind(R.id.pairProfileName)
    TextView pairName;
    @Bind(R.id.pairProfilePhoto)
    CircularImageView pairPhoto;

    @Bind(R.id.userProfilePhoto)
    CircularImageView userPhoto;
    @Bind(R.id.userProfileName)
    TextView userProfileName;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle(R.string.stats);

        DataService.init().getStats(new DataService.onStatsResult() {
            @Override
            public void onStatsResult(StatsData response) {
                if (response.getResult() != null) {

                    userDoneTasks.setText(response.getResult().getMe().getExecutedJob());
                    userExpiredTasks.setText(response.getResult().getMe().getObtainedJob());
                    userNotExecutedTasks.setText(response.getResult().getMe().getDelJob());

                    pairDoneTasks.setText(response.getResult().getPair().getExecutedJob());
                    pairExpiredTasks.setText(response.getResult().getPair().getObtainedJob());
                    pairNotExecutedTasks.setText(response.getResult().getPair().getDelJob());

                }
            }
        }, PrefRepository.getToken(StatsActivity.this));


        userProfileName.setText(SettingsRepository.getUserInfo(realm).getName());
        pairName.setText(SettingsRepository.getUserInfo(realm).getPairName());

        UserInfo userInfo = SettingsRepository.getUserInfo(realm);
        if (!Objects.equals(userInfo.getPhotoReal(), ""))
            Picasso.with(this).load(SettingsRepository.getUserInfo(realm).getPhoto()).into(userPhoto);
        else             Picasso.with(this).load(SettingsRepository.getUserInfo(realm).getPhotoReal()).into(userPhoto);
        Picasso.with(this).load(SettingsRepository.getUserInfo(realm).getPairPhoto()).into(pairPhoto);


    }


}
