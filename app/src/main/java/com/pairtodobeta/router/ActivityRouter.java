package com.pairtodobeta.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.pairtodobeta.data.entities.Task;
import com.pairtodobeta.data.entities.shop.StorageGift;
import com.pairtodobeta.ui.PhotoViewActivity;
import com.pairtodobeta.ui.VKPermissionsActivity;
import com.pairtodobeta.ui.intro.first_task.FirstTaskActivity;
import com.pairtodobeta.ui.intro.invite_couple.InviteCoupleActivity;
import com.pairtodobeta.ui.intro.sign_in.FacebookActivity;
import com.pairtodobeta.ui.intro.sign_in.SignInActivity;
import com.pairtodobeta.ui.intro.sign_up.SignUpActivity;
import com.pairtodobeta.ui.main.MainActivity;
import com.pairtodobeta.ui.main.chat.ChatActivity;
import com.pairtodobeta.ui.main.profile.gifts.GiftsActivity;
import com.pairtodobeta.ui.main.profile.noCouple.SearchCoupleActivity;
import com.pairtodobeta.ui.main.profile.settings.SettingsActivity;
import com.pairtodobeta.ui.main.profile.stats.StatsActivity;
import com.pairtodobeta.ui.main.shop.SendGiftActivity;
import com.pairtodobeta.ui.main.shop.ShopCategoryActivity;
import com.pairtodobeta.ui.main.tasks.JobChatActivity;
import com.pairtodobeta.ui.main.tasks.new_task.AddTaskActivity;
import com.pairtodobeta.ui.main.tasks.task.TaskActivity;
import com.pairtodobeta.ui.main.tasks.task.TaskEditActivity;
import com.pairtodobeta.utils.Constants;

/**
 * Activity navigation.
 */
public class ActivityRouter {

    public static void startSignUpActivity(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startSignInActivity(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startStatsActivity(Context context) {
        Intent intent = new Intent(context, StatsActivity.class);
        context.startActivity(intent);
    }

    public static void startGiftsActivity(Context context) {
        Intent intent = new Intent(context, GiftsActivity.class);
        context.startActivity(intent);
    }

    public static void startSendGiftActivity(Context context, StorageGift storageGift) {
        Intent intent = new Intent(context, SendGiftActivity.class);
        intent.putExtra("StorageGift", storageGift);
        context.startActivity(intent);
    }

    public static void startFirstTaskActivity(Context context) {
        Intent intent = new Intent(context, FirstTaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startAddTaskActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startTaskActivity(Activity context, Task task, int requestCode) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("Task",task);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startEditTaskActivity(Activity context, Task task, int requestCode) {
        Intent intent = new Intent(context, TaskEditActivity.class);
        intent.putExtra("Task",task);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startChatActivity(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    public static void startCategoryShopActivity(Context context, String extra) {
        Intent intent = new Intent(context, ShopCategoryActivity.class);
        intent.putExtra("Category", extra);
        context.startActivity(intent);
    }

    public static void startInviteActivity(Context context) {
        Intent intent = new Intent(context, InviteCoupleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startSearchCoupleActivity(Activity context) {
        Intent intent = new Intent(context, SearchCoupleActivity.class);
        context.startActivityForResult(intent, Constants.REQUEST_SEARCH_PAIR);
    }

    public static void startSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }


    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startVkPermissionsActivity(Context context) {
        Intent intent = new Intent(context, VKPermissionsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startFacebookActivity(Context context) {
        Intent intent = new Intent(context, FacebookActivity.class);
        context.startActivity(intent);
    }

    public static void startJobChatActivity(Context context, Task task) {
        Intent intent = new Intent(context, JobChatActivity.class);
        intent.putExtra("Job", task);
        context.startActivity(intent);
    }

    public static void startPhotoActivity(Context activity, String photoUrl, View imageView, String constAnim){
        Intent intent = new Intent(activity, PhotoViewActivity.class);

        intent.putExtra("Photo",photoUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(constAnim);
            Pair<View, String> pair1 = Pair.create(imageView, imageView.getTransitionName());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity)activity, pair1);
            activity.startActivity(intent, options.toBundle());
        } else
            activity.startActivity(intent);
    }
}

