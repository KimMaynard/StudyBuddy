package com.example.studybuddy.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.studybuddy.R;

public class TopNavigationViewHelper {
    private static final String TAG = "TopNavigationViewHelper";

    public static void setupTopNavigationView(BottomNavigationView bottomNavigationView) {
        Log.d(TAG, "setupTopNavigationView: Setting up navigationView");

    }

    public static void enableNavigation(final Context context, BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(item -> {
            Intent i;
            int itemId = item.getItemId();
            if (itemId == R.id.ic_profile) {
                i = new Intent(context, SettingsActivity.class);
                context.startActivity(i);
                return true;
            } else if (itemId == R.id.ic_matched) {
                i = new Intent(context, MatchesActivity.class);
                context.startActivity(i);
                return false;
            } else {
                Log.d(TAG, "Unhandled item selected: " + itemId);
                return false;
            }

        });
    }


}

