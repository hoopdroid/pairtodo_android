package com.pairtodopremium.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.userPhoto.UserImage;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.event.GIftEvent;
import com.pairtodopremium.event.IEvent;
import com.pairtodopremium.event.JobEvent;
import com.pairtodopremium.event.JobMessageEvent;
import com.pairtodopremium.event.MessageEvent;
import com.pairtodopremium.event.PairAddEvent;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.router.FragmentRouter;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.ui.main.profile.ProfileFragment;
import com.pairtodopremium.ui.main.shop.ShopFragment;
import com.pairtodopremium.ui.main.tasks.TasksFragment;
import com.pairtodopremium.utils.AnimationBuilderHelper;
import com.pairtodopremium.utils.Constants;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.pairtodopremium.utils.FileUtil.reduceFile;

public class MainActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.bottomBar)
    BottomBar mBottomBar;
    @Bind(R.id.paradelImageToolbar)
    ImageView pairToDoImage;
    @Bind(R.id.mainFrame)
    ViewGroup mainFrame;

    boolean isOpened = false;
    boolean isMainTabOpened = true;

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_favorites) {
                    mBottomBar.getTabWithId(tabId).removeBadge();
                    ViewCompat.setElevation(mainFrame, convertDpToPixel(0, MainActivity.this));
                    FragmentRouter.showMainTabFragment(MainActivity.this, ProfileFragment.newInstance());
                    isMainTabOpened = true;
                }

                if (tabId == R.id.tab_tasks) {
                    mBottomBar.getTabWithId(tabId).removeBadge();
                    ViewCompat.setElevation(mainFrame, convertDpToPixel(4, MainActivity.this));
                    FragmentRouter.showMainTabFragment(MainActivity.this, TasksFragment.newInstance());
                    isMainTabOpened = false;
                }

                if (tabId == R.id.tab_shop) {
                    mBottomBar.getTabWithId(tabId).removeBadge();
                    ViewCompat.setElevation(mainFrame, convertDpToPixel(0, MainActivity.this));
                    FragmentRouter.showMainTabFragment(MainActivity.this, ShopFragment.newInstance());
                    isMainTabOpened = false;
                }
            }
        });

        AnimationBuilderHelper.startIntroToolbarAnimation(this, pairToDoImage);
        AnimationBuilderHelper.startIntroBottomAnimation(this, mBottomBar);

    }

    private void openShopScreen() {
        mBottomBar.getTabWithId(R.id.tab_shop).removeBadge();
        mBottomBar.selectTabWithId(R.id.tab_shop);
    }

    private void openProfileScreen() {
        mBottomBar.getTabWithId(R.id.tab_favorites).removeBadge();
        mBottomBar.selectTabWithId(R.id.tab_favorites);
    }

    private void openTasksScreen() {
        mBottomBar.getTabWithId(R.id.tab_tasks).removeBadge();
        mBottomBar.selectTabWithId(R.id.tab_tasks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CHANGE_TASK:
                    FragmentRouter.showMainTabFragment(MainActivity.this, TasksFragment.newInstance());
                    break;

                case Constants.REQUEST_SEARCH_PAIR:
                    FragmentRouter.removeContentFragment(MainActivity.this);
                    FragmentRouter.showMainTabFragment(MainActivity.this, ProfileFragment.newInstance());
                    break;
            }

            initGalleryCallback(requestCode, resultCode, data);

        } else {
        }
    }

    public void initGalleryCallback(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(MainActivity.this, "Ошибка загрузки! Попробуйте еще раз!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                try {
                    DataService.init().uploadUserImage(reduceFile(imageFile), new DataService.onUploadUserPhoto() {
                        @Override
                        public void onUploadResult(UserImage image) {
                            FragmentRouter.showMainTabFragment(MainActivity.this, ProfileFragment.newInstance());
                        }

                        @Override
                        public void onUploadError(String error) {
                            FragmentRouter.showMainTabFragment(MainActivity.this, ProfileFragment.newInstance());
                        }
                    }, PrefRepository.getToken(MainActivity.this));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(MessageEvent event) {
        createAlertNotification(getString(R.string.new_message_from_couple), event.getMessage(), event);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onJobEvent(JobEvent event) {
        createAlertNotification(getString(R.string.you_have_changes), "", event);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onJobMessageEvent(JobMessageEvent event) {
        createAlertNotification(getString(R.string.new_message_from_couple), "", event);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onGIftEvent(GIftEvent event) {
        createAlertNotification(getString(R.string.you_got_gift), "", event);
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPairAddEvent(PairAddEvent event) {
        if (isMainTabOpened) {
            FragmentRouter.removeContentFragment(MainActivity.this);
            FragmentRouter.showMainTabFragment(this, ProfileFragment.newInstance());
        }
        else
            createAlertNotification(getString(R.string.couple_joined), "", event);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void createAlertNotification(String title, String message, final IEvent event) {
        final Alerter alerter = Alerter.create(this)
                .setTitle(title)
                .setText(message)
                .setDuration(5000)
                .setBackgroundColor(R.color.colorPrimary)
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        if (!isOpened) {
                            addBadges(event);
                            isOpened = false;
                        }

                    }
                });

        alerter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerter.hide();
                isOpened = true;
                selectGoToScreen(event);
            }
        });

        alerter.show();
    }

    private void selectGoToScreen(IEvent event) {
        if (event instanceof MessageEvent)
            ActivityRouter.startChatActivity(this);
        if (event instanceof JobEvent)
            openTasksScreen();
        if (event instanceof JobMessageEvent)
            openTasksScreen();
        if (event instanceof PairAddEvent)
            openProfileScreen();
        if (event instanceof GIftEvent)
            ActivityRouter.startGiftsActivity(this);

    }

    private void addBadges(IEvent event) {
        if (event instanceof MessageEvent)
            mBottomBar.getTabWithId(R.id.tab_favorites).setBadgeCount(1);
        if (event instanceof JobEvent)
            mBottomBar.getTabWithId(R.id.tab_tasks).setBadgeCount(1);
        if (event instanceof JobMessageEvent)
            mBottomBar.getTabWithId(R.id.tab_tasks).setBadgeCount(1);
        if (event instanceof PairAddEvent)
            mBottomBar.getTabWithId(R.id.tab_favorites).setBadgeCount(1);
        if (event instanceof GIftEvent)
            mBottomBar.getTabWithId(R.id.tab_favorites).setBadgeCount(1);

    }


}
