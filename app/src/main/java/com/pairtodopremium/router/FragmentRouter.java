package com.pairtodopremium.router;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.pairtodopremium.R;

/**
 * Fragment navigation.
 */
public class FragmentRouter {

    public static void showMainTabFragment(Activity activity, Fragment fragment) {

        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();

    }


    public static void removeContentFragment(Activity activity) {

        if (activity.getFragmentManager().findFragmentById(R.id.mainFrame) != null)
            activity.getFragmentManager().
                    beginTransaction().remove(activity.getFragmentManager().findFragmentById(R.id.mainFrame)).commit();
    }
}