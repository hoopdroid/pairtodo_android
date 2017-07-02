package com.pairtodopremium.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import com.roughike.bottombar.BottomBar;

/**
 * Created by Илья on 23.07.2016.
 */

public class AnimationBuilderHelper {

  public static void startIntroToolbarAnimation(Context context, View mToolbar) {

    int actionbarSize = dpToPx(context, 56);
    mToolbar.setTranslationY(-actionbarSize);

    mToolbar.animate()
        .translationY(0)
        .setDuration(Constants.ANIM_DURATION_TOOLBAR)
        .setStartDelay(400);
  }

  public static void startIntroFabAnimation(Context context, FloatingActionButton fab) {

    int actionbarSize = dpToPx(context, 56);

    fab.setAlpha(0.0f);

    fab.setVisibility(View.VISIBLE);

    fab.animate().alpha(1f).setDuration(Constants.ANIM_DURATION_TOOLBAR).setStartDelay(800);
  }

  public static void startIntroViewAnimation(Context context, View fab) {

    int actionbarSize = dpToPx(context, 56);

    fab.setAlpha(0.2f);

    fab.setVisibility(View.VISIBLE);

    fab.animate().alpha(1f).setDuration(Constants.ANIM_DURATION_TOOLBAR).setStartDelay(500);
  }

  public static void startIntroBottomAnimation(Context context, BottomBar mBottomBar) {

    int actionbarSize = dpToPx(context, 56);
    mBottomBar.setTranslationY(actionbarSize);
    mBottomBar.animate()
        .translationY(mBottomBar.getY() - actionbarSize)
        .setDuration(Constants.ANIM_DURATION_TOOLBAR)
        .setStartDelay(600);
  }

  private static int dpToPx(Context context, int dp) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return px;
  }

  public static void startRVAnimation(Activity activity, RecyclerView recyclerView) {

    Display display = activity.getWindowManager().getDefaultDisplay();
    int height = display.getHeight();

    recyclerView.setTranslationY(height);
    recyclerView.setAlpha(0f);
    recyclerView.animate()
        .translationY(0)
        .setDuration(600)
        .alpha(1f)
        .setInterpolator(new FastOutSlowInInterpolator())
        .start();
  }
}
